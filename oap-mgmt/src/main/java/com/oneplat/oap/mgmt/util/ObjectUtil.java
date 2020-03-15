/*
 * Copyright (c) 2011 SK Telecom.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK Telecom.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK Telecom.
 */
package com.oneplat.oap.mgmt.util;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 객체의 로딩을 지원하는 유틸 클래스
 * <p>
 * <b>NOTE:</b> Class 로딩 등의 객체관련 기능을 제공하는 유틸이다.
 * @author 실행환경 개발팀 윤성종
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.06.01  윤성종           최초 생성
 * 
 * </pre>
 */
public class ObjectUtil {

    /** The log. */
    private static Log log = LogFactory.getLog(ObjectUtil.class);

    /**
     * Instantiates a new object util.
     */
    private ObjectUtil() {

    }

    /**
     * 클래스명으로 객체를 로딩한다.
     *
     * @param className the class name
     * @return the class
     * @throws ClassNotFoundException the class not found exception
     * @throws Exception the exception
     */
    public static Class<?> loadClass(String className)
            throws ClassNotFoundException, Exception {

        Class<?> clazz = null;
        try {
            clazz =
                Thread.currentThread().getContextClassLoader().loadClass(
                    className);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException();
        } catch (Exception e) {
            throw new Exception(e);
        }

        if (clazz == null) {
            clazz = Class.forName(className);
        }

        return clazz;

    }

    /**
     * 클래스명으로 객체를 로드한 후 인스턴스화 한다.
     *
     * @param className the class name
     * @return the object
     * @throws ClassNotFoundException the class not found exception
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws Exception the exception
     */
    public static Object instantiate(String className)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, Exception {
        Class<?> clazz;

        try {
            clazz = loadClass(className);
            return clazz.newInstance();
        } catch (ClassNotFoundException e) {
            if (log.isErrorEnabled())
                log.error(className + " : Class is can not instantialized.");
            throw new ClassNotFoundException();
        } catch (InstantiationException e) {
            if (log.isErrorEnabled())
                log.error(className + " : Class is can not instantialized.");
            throw new InstantiationException();
        } catch (IllegalAccessException e) {
            if (log.isErrorEnabled())
                log.error(className + " : Class is not accessed.");
            throw new IllegalAccessException();
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error(className + " : Class is not accessed.");
            throw new Exception(e);
        }
    }

    /**
     * 클래스명으로 파라매터가 있는 클래스의 생성자를 인스턴스화 한다. 예) Class <?>
     * clazz = EgovObjectUtil.loadClass(this.mapClass);
     * Constructor <?> constructor =
     * clazz.getConstructor(new Class
     * []{DataSource.class, String.class}); Object []
     * params = new Object []{getDataSource(),
     * getUsersByUsernameQuery()};
     * this.usersByUsernameMapping =
     * (EgovUsersByUsernameMapping)
     * constructor.newInstance(params);
     *
     * @param className the class name
     * @param types the types
     * @param values the values
     * @return the object
     * @throws ClassNotFoundException the class not found exception
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws Exception the exception
     */
    public static Object instantiate(String className, String[] types,
            Object[] values) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, Exception {
        Class<?> clazz;
        Class<?>[] classParams = new Class[values.length];
        Object[] objectParams = new Object[values.length];

        try {
            clazz = loadClass(className);

            for (int i = 0; i < values.length; i++) {
                classParams[i] = loadClass(types[i]);
                objectParams[i] = values[i];
            }

            Constructor<?> constructor = clazz.getConstructor(classParams);
            return constructor.newInstance(values);

        } catch (ClassNotFoundException e) {
            if (log.isErrorEnabled())
                log.error(className + " : Class is can not instantialized.");
            throw new ClassNotFoundException();
        } catch (InstantiationException e) {
            if (log.isErrorEnabled())
                log.error(className + " : Class is can not instantialized.");
            throw new InstantiationException();
        } catch (IllegalAccessException e) {
            if (log.isErrorEnabled())
                log.error(className + " : Class is not accessed.");
            throw new IllegalAccessException();
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error(className + " : Class is not accessed.");
            throw new Exception(e);
        }
    }

    /**
     * 객체가 Null 인지 확인한다.
     *
     * @param object the object
     * @return Null인경우 true / Null이 아닌경우 false
     */
    public static boolean isNull(Object object) {
        return ((object == null) || object.equals(null));
    }
}
