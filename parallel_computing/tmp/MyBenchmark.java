package com.Dom;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

public class MyBenchmark {
	@State(Scope.Benchmark)
		public static class GameState {
			public ConcurrentHashMap<Integer,Merchant> merchants = new ConcurrentHashMap<Integer, Merchant>(100);
			public Random rand = new Random();
			public GameState(){
				for (int i = 0; i < 30; i++){
					long seed = rand.nextLong();
					Integer id = new Integer(i);
					Merchant m = new Merchant(seed, i);
					merchants.put(id, m);
				}
			}
		}

	@State(Scope.Thread)
		public static class MyState {
			public Random rand = new Random();
			public double behavior = rand.nextDouble();
			public double salary = (rand.nextDouble() * rand.nextInt(100000));
	}

    @Benchmark @BenchmarkMode(Mode.All) @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void buy(GameState state, MyState mystate){
        // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
        // Put your benchmark code here.

    	Merchant myMerchant = null;
    	while(myMerchant == null){
    		myMerchant = state.merchants.get(state.rand.nextInt(Merchant.getCount()));
    	}
    	Good good = myMerchant.getGoods()[state.rand.nextInt(myMerchant.getGoodCount())];
    	if (good.getPrice() < mystate.salary){
    		good.buy();
    		mystate.salary -= good.getPrice();
    	}
    }

    @Benchmark @BenchmarkMode(Mode.All) @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void interact(GameState state, MyState mystate){
        if (state.merchants.isEmpty()){
            return;
        }

    	System.out.println("Size: " + state.merchants.size());
    	Merchant myMerchant = null;
    	while(myMerchant == null){
    		myMerchant = state.merchants.get(state.rand.nextInt(Merchant.getCount()));
    	}

    	if (Math.random() >= mystate.behavior){
    		if (myMerchant.badInteraction()){
    			if (state.merchants.remove(myMerchant.id()) == null){
    				return;
    			}
    		}
    	} else {
    		if (myMerchant.goodInteraction()){
    			if (state.merchants.put(Merchant.getCount(), new Merchant(state.rand.nextLong(), Merchant.getCount())) != null){
    				return;
    			}
    		}
    	}
    }
}
