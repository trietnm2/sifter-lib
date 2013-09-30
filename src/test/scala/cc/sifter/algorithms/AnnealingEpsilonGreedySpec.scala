package cc.sifter.algorithms

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

import cc.sifter.Arm
import java.util.Random

class AnnealingEpsilonGreedySpec extends FlatSpec {
  val rand = new Random(1)
  
  "An AnnealingEpsilonGreedy algorithm" should "produce the right steady state output" in {
      
    val Npulls = 10000
    val test = AnnealingEpsilonGreedy(Seq(Arm("one"), Arm("two"), Arm("three")))
    
    for (i <- 1 to Npulls) {
      val arm = test.selectArm()
      val prob = arm.getId match {
        case "one"   => .2
        case "two"   => .4
        case "three" => .8   // this should be the highest
      }

      if (rand.nextDouble < prob) {
          test.success(arm, 1.0)
      }
      else {
          test.failure(arm, 1.0)
      }
    }

    test.arms(2).getPullCount should be > (test.arms(0).getPullCount)
    test.arms(2).getPullCount should be > (test.arms(1).getPullCount)

    test.arms(2).getValue should be > (test.arms(0).getValue)
    test.arms(2).getValue should be > (test.arms(1).getValue)
    test.arms(1).getValue should be > (test.arms(0).getValue)
  }
}
