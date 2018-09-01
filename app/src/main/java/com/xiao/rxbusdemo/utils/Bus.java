package com.xiao.rxbusdemo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by zhangxiao on 2018/5/22 0022.
 */
public class Bus {

    //<tag,subject>,每一个标签对应一个subject
    private static Map<String, Subject> mSubjectMap = new HashMap<>();
    //<className,disposable>,每一个类对应一个disposable
    private static Map<String,CompositeDisposable> sDisposableMap=new HashMap<>();
    //<className,tags>,每一个类对应一个tag集合
    private static Map<String,List<String>> sTagsMap=new HashMap<>();

    /**
     * 获取订阅关系
     * @param className
     * @return
     */
    private static CompositeDisposable getDisposable(String className){
        CompositeDisposable compositeDisposable=sDisposableMap.get(className);
        if (compositeDisposable==null){
            compositeDisposable=new CompositeDisposable();
            sDisposableMap.put(className,compositeDisposable);
        }
        return compositeDisposable;
    }

    /**
     * 获取被观察者的标签
     * @param className
     * @return
     */
    private static List<String> getTags(String className){
        List<String> tags=sTagsMap.get(className);
        if (tags==null){
            tags=new ArrayList<>();
            sTagsMap.put(className,tags);
        }
        return tags;
    }

    /**
     * 从mSubjectMap里获取subject,如果没有则创建一个subject,并添加到mSubjectMap
     *
     * @param tag
     * @return
     */
    private static synchronized <T> Subject<T> getSubject(String tag) {
        if (mSubjectMap.get(tag) == null) {
            BehaviorSubject<T> subject = BehaviorSubject.create();
            mSubjectMap.put(tag, subject);
            return subject;
        }
        return mSubjectMap.get(tag);
    }

    /**
     * 发送事件
     * @param tag
     * @param obj
     */
    public static void post(String tag,Object obj){
        getSubject(tag).onNext(obj);
    }

    /**
     * 订阅事件,事件响应在主线程
     * @param tag
     * @param consumer
     * @param <T>
     */
    public static <T> void subscribe(String tag, Consumer<T> consumer){
        StackTraceElement[] stackTraceElements = (new Throwable()).getStackTrace();
        String className=stackTraceElements[1].getClassName();
        if (className.contains("$")){
            className=className.substring(0,className.indexOf("$"));
        }
        getTags(className).add(tag);
        getDisposable(className).add(Bus.<T>getSubject(tag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer));
    }

    /**
     * 清除已添加管理的订阅关系
     */
    public static void clear() {
        StackTraceElement[] stackTraceElements = (new Throwable()).getStackTrace();
        String className=stackTraceElements[1].getClassName();
        if (className.contains("$")){
            className=className.substring(0,className.indexOf("$"));
        }
        getDisposable(className).clear();
        sDisposableMap.remove(className);
        for (String tag : getTags(className)) {
            //如果tag对应的subject已经没有观察者,则删除掉该subject
            if (!Bus.getSubject(tag).hasObservers()) {
                mSubjectMap.remove(tag);
            }
        }
        sTagsMap.remove(className);
    }


}
