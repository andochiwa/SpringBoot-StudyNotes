package com.github.boot.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HAN
 * @version 1.0
 * @create 03-11-4:15
 */
public class TestReactor {

    public static void main(String[] args){
        // just方法直接声明
        Flux.just(1, 2, 3, 4).subscribe(System.out::println);
        Mono.just(1).subscribe(System.out::println);

        // 其他方法
//        Integer[] array = {1, 2, 3, 4};
//        Flux.fromArray(array);
//
//        List<Integer> list = Arrays.asList(array);
//        Flux.fromIterable(list);
//
//        Stream<Integer> stream = list.stream();
//        Flux.fromStream(stream);
    }

}
