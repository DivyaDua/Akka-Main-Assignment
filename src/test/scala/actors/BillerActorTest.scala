package actors

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import models.Biller
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import org.scalatest.mockito.MockitoSugar

class BillerActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar {

  val databaseActorRef = TestProbe()
  val billerActorRef: ActorRef = system.actorOf(BillerActor.props(databaseActorRef.ref))

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  test("testing link account number with biller case") {
    databaseActorRef.setAutoPilot((sender: ActorRef, msg: Any) => {
      val result = msg match {
        case (accountNumber: Long, biller: Biller) => "Biller is successfully linked"
      }
      sender ! result
      TestActor.NoAutoPilot
    })
    billerActorRef ! (1000L,Biller("phone", "PhoneBiller", 1000L))
    expectMsg("Biller is successfully linked")
  }

  test("testing already linked biller case") {
    databaseActorRef.setAutoPilot((sender: ActorRef, msg: Any) => {
      val result = msg match {
        case (accountNumber: Long, biller: Biller) =>
          "Biller of category phone is already linked with this account"
      }
      sender ! result
      TestActor.NoAutoPilot
    })
    billerActorRef ! (900L,Biller("phone", "PhoneBiller", 900L))
    expectMsg("Biller of category phone is already linked with this account"
    )
  }

  test("testing for wrong bill category case"){
    billerActorRef ! (1000L,Biller("abc", "AbcBiller", 1000L))
    expectMsg("Biller of such category cannot be linked")
  }

  test("testing for invalid inputs case"){
      billerActorRef ! 12
      expectMsg("Invalid Inputs")
  }

}
