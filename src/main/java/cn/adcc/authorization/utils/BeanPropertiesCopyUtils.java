package cn.adcc.authorization.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class BeanPropertiesCopyUtils {

    public static <T> T copyNotNull(T origin, T current, String... ignoredProperties) {
        List<String> ignoredProperties1 = Arrays.asList(ignoredProperties);
        Class clazz = current.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                String propertyName = field.getName();
                if (ignoredProperties1.contains(propertyName)) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(propertyName, clazz);
                Method getterMethod = pd.getReadMethod();
                if (getterMethod.invoke(current) != null){
                    Method setterMethod = pd.getWriteMethod();
                    setterMethod.invoke(origin, getterMethod.invoke(current));
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return origin;
    }
}
