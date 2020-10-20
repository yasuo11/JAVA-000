package com.geekbang.week1;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {

    private static final String filePath = "/Users/apple/Desktop/javaClass/src/main/java/com/geekbang/week1/Hello.xlass";
    private static final String className = "Hello";
    private static final String methodName = "hello";
    private static final Integer readOnceMaxLen = 1024;

    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = readClassFile(filePath);

        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }

        return defineClass(className, bytes, 0, bytes.length);
    }


    private byte[] readClassFile(String fileName) {
        File classFile = new File(fileName);
        if(classFile.length() > Integer.MAX_VALUE) {
            throw new RuntimeException("file length is too long!");
        }

        byte[] res = new byte[(int) classFile.length()];
        try (FileInputStream fileInputStream = new FileInputStream(classFile)) {
            int offset = 0;
            int readCnt;

            while ((readCnt = fileInputStream.read(res, offset, Math.min(readOnceMaxLen, res.length - offset))) > 0) {
                offset += readCnt;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return res;
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        MyClassLoader myClassLoader = new MyClassLoader();

        Class<?> helloClass = myClassLoader.findClass(filePath);

        Method method = helloClass.getMethod(methodName);

        method.invoke(helloClass.newInstance());
    }
}
