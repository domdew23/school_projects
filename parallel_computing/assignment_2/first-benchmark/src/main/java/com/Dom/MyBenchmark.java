/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

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
