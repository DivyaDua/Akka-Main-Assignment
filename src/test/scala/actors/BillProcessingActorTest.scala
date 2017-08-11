package actors

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

class BillProcessingActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar {

  val databaseActorRef = TestProbe()
  val billProcessingActorRef: ActorRef = system.actorOf(BillProcessingActor.props(databaseActorRef.ref))

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  databaseActorRef.setAutoPilot((sender: ActorRef, msg: Any) => {
    val result = msg match {
      case (accountNumber: Long, billAmount: Double, billerCategory: String) => "Bill paid"
    }
    sender ! result
    TestActor.KeepRunning
  })

  test("testing car bill processing actor"){
    billProcessingActorRef ! (900L, "car")
    expectMsg("Bill paid")
  }

  test("testing phone bill processing actor"){
    billProcessingActorRef ! (900L, "phone")
    expectMsg("Bill paid")
  }

  test("testing electricity bill processing actor"){
    billProcessingActorRef ! (900L, "electricity")
    expectMsg("Bill paid")
  }

  test("testing internet bill processing actor"){
    billProcessingActorRef ! (900L, "internet")
    expectMsg("Bill paid")
  }

  test("testing food bill processing actor"){
    billProcessingActorRef ! (900L, "food")
    expectMsg("Bill paid")
  }

  test("testing for invalid inputs"){
    billProcessingActorRef ! 1
    expectMsg("Invalid Inputs")
  }

}
