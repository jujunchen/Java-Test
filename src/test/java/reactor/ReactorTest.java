package reactor;

import java.time.Duration;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

public class ReactorTest {

	@Test
	public void test1() throws InterruptedException {
//		Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape");
		
//		StepVerifier.create(fruitFlux).expectNext("Apple").expectNext("Orange").expectNext("Grape1").verifyComplete();
		
//		Flux<Integer> integerFlux = (Flux<Integer>) Flux.range(0, 10).subscribe(i ->System.out.print(i));
		
//		Flux<Long> inteFlux = (Flux<Long>) Flux.interval(Duration.ofSeconds(1)).take(2);
//		StepVerifier.create(inteFlux).expectNext(0L).expectNext(1L).verifyComplete();
		
		//zip合并
		Flux<String> nameFlux = Flux.just("Garfield", "Kojak", "Barbossa", "Barbossa");
		Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples");
		Flux<Tuple2<String, String>> ziFlux = Flux.zip(nameFlux, foodFlux);
		ziFlux.subscribe(System.out::print);
		//合并函数
//		Flux.zip(nameFlux, foodFlux, (c,f) -> c + " eats " + f).subscribe(System.out::println);
		
		//选择第一个发布的值
		//订阅后100ms开始发布
//		Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth").delaySubscription(Duration.ofMillis(100));
//		Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");
//		Flux<String> firstFlux = Flux.first(slowFlux, fastFlux);
//		firstFlux.subscribe(System.out::println);
		
		//过滤
//		nameFlux.filter(item -> item.contains("a")).subscribe(System.out::println);
		//去重
//		nameFlux.distinct().subscribe(System.out::println);
		//buffer缓冲
//		Flux<List<String>> newNameFlux = nameFlux.buffer(3);
//		newNameFlux.subscribe(item -> System.out.println(item.size()));
		//异步转换
//		newNameFlux.flatMap(x -> Flux.fromIterable(x).map(String::toLowerCase).subscribeOn(Schedulers.parallel()).log()).subscribe();
//		nameFlux.flatMap(n -> Mono.just(n).map(String::toLowerCase).subscribeOn(Schedulers.parallel())).subscribe(System.out::println);
		//collectMap
//		nameFlux.collectMap(a -> a.charAt(0)).subscribe(item -> System.out.println(item.keySet()));
		//all，any判断,all都符合条件，any任何一个符合条件
		nameFlux.all(item -> item.contains("a")).subscribe(System.out::println);
		
		Thread.sleep(1000);
	}

}
