package cn.chengzhiya.langutil.manager.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionManager {
    /**
     * 新建指定类实例的类对象
     *
     * @param clazz 类实例
     * @param args  传入参数
     * @return 类对象
     */
    public <T> T newClass(Class<?> clazz, Object... args) {
        try {
            return (T) clazz.getConstructor().newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过反射指定类实例获取指定方法的方法实例
     *
     * @param clazz      类实例
     * @param methodName 方法名称
     * @param accessible 强制访问
     * @param argsTypes  传参类型
     * @return 方法实例
     */
    public Method getMethod(Class<?> clazz, String methodName, boolean accessible, Class<?>... argsTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, argsTypes);
            method.setAccessible(accessible);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过反射指定类实例获取指定方法的变量实例
     *
     * @param clazz      类实例
     * @param fieldName  变量名称
     * @param accessible 强制访问
     * @return 变量实例
     */
    public Field getField(Class<?> clazz, String fieldName, boolean accessible) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(accessible);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过反射指定方法实例获取返回值
     *
     * @param method 方法实例
     * @param object 对象实例
     * @param args   传入参数
     * @return 返回值
     */
    public <T> T invokeMethod(Method method, Object object, Object... args) {
        try {
            Object invokeObject = method.invoke(object, args);
            if (invokeObject == null) {
                return null;
            }
            return (T) invokeObject;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取反射指定变量实例的返回值
     *
     * @param field  变量实例
     * @param object 对象实例
     */
    public <T> T getFieldValue(Field field, Object object) {
        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过反射指定变量实例的返回值
     *
     * @param field  变量实例
     * @param object 对象实例
     * @param value  修改的值
     */
    public void setFieldValue(Field field, Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
