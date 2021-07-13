package me.zhixingye.im.util;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Stack;

import javax.annotation.Nullable;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月11日.
 */
public class ReflectUtil {

    /**
     * 从给定的sourceCls中找出hasGenericCls里面包含的第genericIndex个泛型
     * 比如public MyClass extends GenericClass<String>，通过这个方法能找到String.class
     * 支持泛型嵌套的传递场景，找到后会返回对应的泛型定义类，否在返回null。
     *
     * @param sourceCls     给定的class，需要从这个class中找到对应hasGenericCls的泛型定义类
     * @param hasGenericCls 包含泛型的class，该class一般都是sourceCls的父类
     * @param genericIndex  泛型的位置，从0开始
     * @param <T>           任意泛型
     * @return 返回泛型的最终的定义类，如果找不到就返回null。
     */
    @Nullable
    public static <T> Class<?> findGenericClass(Class<? extends T> sourceCls, Class<T> hasGenericCls, int genericIndex) {
        // 基本参数判断，包括给定的hasGenericCls是否含有泛型声明
        if (sourceCls == null || hasGenericCls == null || hasGenericCls.getTypeParameters().length <= genericIndex) {
            return null;
        }
        // 创建继承关系栈
        Stack<Type> clsInheritanceStack = createClassInheritanceStack(sourceCls, hasGenericCls);
        if (clsInheritanceStack.empty()) {
            return null;
        }

        // 开始便利继承关系栈，栈顶一般是hasGenericCls本身，而栈底一般是sourceCls本身
        // 这里从栈顶开始，自顶而下查找
        String typeVariableName = null;
        while (!clsInheritanceStack.empty()) {
            Type type = clsInheritanceStack.pop();
            // 因为是泛型，所以在找到具体的class之前的所有type都肯定是ParameterizedType
            if (!(type instanceof ParameterizedType)) {
                return null;
            }

            ParameterizedType pType = (ParameterizedType) type;
            Class<?> cls = getClassType(pType);
            // 如果无法从Type中找到class，一般是未知场景，先直接return
            if (cls == null) {
                return null;
            }

            //拿到当前claas的泛型声明，这里的泛型声明一般是那种T,V等泛型声明符号
            TypeVariable<?>[] typeParameterArr = cls.getTypeParameters();
            // 判断是否为空，为空表示第一次进循环，这个时候因为栈顶元素就是hasGenericCls
            // 所以特殊判断一下这种情况，看看这个时候hasGenericCls是否已经能拿到泛型定义class，不能说明是泛型嵌套
            if (typeVariableName == null) {
                Type actualType = pType.getActualTypeArguments()[genericIndex];
                // 判断是否已经是具体的泛型定义class
                if (actualType instanceof Class) {
                    return (Class<?>) actualType;
                }
                // 否在说明是泛型嵌套，这个时候就需要把泛型声明符号记录起来
                // 后续循环查找中就会继续查找该泛型声明符号具体的定义class
                if (actualType instanceof TypeVariable) {
                    typeVariableName = ((TypeVariable<?>) actualType).getName();
                    continue;
                }
            }

            // 因为泛型支持嵌套，并且顺序不固定，所以这里需要开始遍历所有的泛型声明符号
            // 直到找到对应的泛型定义class，如果还是只能摘到泛型声明符号说明还有嵌套，还要继续下一论继承关系循环
            for (int i = 0, length = typeParameterArr.length; i < length; i++) {
                TypeVariable<?> typeVariable = typeParameterArr[i];
                // 判断当前待检查的泛型声明符号是否和目标泛型声明符号相同
                // 相同表示找到了泛型的位置(需要先找到位置，因为一个类可以声明多个泛型)
                if (TextUtils.equals(typeVariable.getName(), typeVariableName)) {
                    // 找到位置之后尝试获取泛型定义类
                    Type mayBeHasGenericActualTyp = pType.getActualTypeArguments()[i];

                    // 如果是class表示找到了定义类
                    if (mayBeHasGenericActualTyp instanceof Class) {
                        return (Class<?>) mayBeHasGenericActualTyp;
                    }

                    //如果还是TypeVariable说明有嵌套，还要继续下一论继承循环
                    if (mayBeHasGenericActualTyp instanceof TypeVariable) {
                        typeVariableName = ((TypeVariable<?>) mayBeHasGenericActualTyp).getName();
                        break;
                    }

                    //如果不输入上面2个场景，那剩下情况就是一些未知的情况，直接返回null
                    return null;

                }
            }
        }
        return null;
    }

    /**
     * 给定子类(subclass)和父类(parentClass)，生成子类到父类的继承关系栈
     * 栈中越靠近栈顶的class表示约靠近最终的父类。
     *
     * @param subclass    子类
     * @param parentClass 子类对应的父类
     * @return 类到父类的继承关系栈
     */
    @NonNull
    private static Stack<Type> createClassInheritanceStack(Class<?> subclass, Class<?> parentClass) {
        Stack<Type> stack = new Stack<>();

        // 如果子类根本不 继承/实现 父类，那么直接返回
        if (!isAssignableFrom(subclass, parentClass)) {
            return stack;
        }

        Type lastAssignableParentClsType = null;

        // 如果子类本身不是一个接口类型的class
        if (!subclass.isInterface()) {
            // 先遍历继承关系，一直往上遍历，直到mayBeAssignableParentClsType已经不属于parentClass的子类为止
            Type mayBeAssignableParentClsType = subclass;
            while (isAssignableFrom(mayBeAssignableParentClsType, parentClass)) {
                lastAssignableParentClsType = mayBeAssignableParentClsType;
                stack.push(lastAssignableParentClsType);
                Class<?> mayBeAssignableParentCls = getClassType(mayBeAssignableParentClsType);
                // 一般是不可能为空，为了安全还是判断了一下
                if (mayBeAssignableParentCls == null) {
                    break;
                }
                mayBeAssignableParentClsType = mayBeAssignableParentCls.getGenericSuperclass();
            }
        }

        // 不等于空表示给定的subclass不是接口类型的class
        if (lastAssignableParentClsType != null) {
            subclass = getClassType(lastAssignableParentClsType);
        }

        // 经过上面的遍历之后已经找到最深的parentClass的子类，现在开始遍历接口class
        // 因为parentClass可以是一个接口class，所以需要遍历接口以找寻可能存在的接口实现关系
        // 而且因为接口也是可以继承接口，所以接口本身也需要继续遍历
        Type[] interfaceArr = subclass.getGenericInterfaces();
        while (interfaceArr != null && interfaceArr.length > 0) {
            boolean hasNextInheritance = false;
            // 遍历同一实现层级的所有接口
            for (Type interfaceType : interfaceArr) {
                if (isAssignableFrom(interfaceType, parentClass)) {
                    stack.push(interfaceType);
                    // 如果找到了对应的parentClass的子接口，就获取该接口的父接口
                    // 因为接口是可以继承接口的，所以这里需要赋值更深层次的接口以便下一轮深度遍历
                    Class<?> interfaceClass = getClassType(interfaceType);
                    // 一般是不可能为空，为了安全还是判断了一下
                    if (interfaceClass != null) {
                        interfaceArr = interfaceClass.getGenericInterfaces();
                        hasNextInheritance = true;
                    }
                    break;
                }
            }
            // hasNextInheritance为false表示已经无法找到任何与parentClass有关的子接口了，说明找完了
            if (!hasNextInheritance) {
                interfaceArr = null;
            }
        }

        return stack;
    }

    /**
     * 判断subclass是否是parentClass的子类。
     *
     * @param subclass    给定的子类
     * @param parentClass 给定的父类
     * @return 如果subclass是parentClass的子类就返回true。
     */
    private static boolean isAssignableFrom(Type subclass, Class<?> parentClass) {
        if (subclass == null) {
            return false;
        }
        if (subclass instanceof Class) {
            return parentClass.isAssignableFrom((Class<?>) subclass);
        }
        if (subclass instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) subclass;
            return isAssignableFrom(pType.getRawType(), parentClass);
        }
        return false;
    }

    /**
     * 从给定的Type中获取该Type中包含的具体的Class
     *
     * @param type 给定的type，可能是class或者是ParameterizedType.
     * @return 返回type对应包含的Class，如果找不到就返回null.
     */
    @Nullable
    private static Class<?> getClassType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return getClassType(((ParameterizedType) type).getRawType());
        }
        return null;
    }

}
