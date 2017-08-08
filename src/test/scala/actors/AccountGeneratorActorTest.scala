package actors

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

class AccountGeneratorActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar{

  val databaseActorRef = TestProbe()
  val accountGeneratorActorRef: ActorRef = system.actorOf(AccountGeneratorActor.props(databaseActorRef.ref))

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  test("testing account created case") {
    databaseActorRef.setAutoPilot((sender: ActorRef, msg: Any) => {
      val result = msg match {
        case (username: String,userDetails: List[String]) => (username, "Account Created")
      }
      sender ! result
      TestActor.NoAutoPilot
    })
    accountGeneratorActorRef ! List("Shruti","mzn", "shruti", "1000.0")
    expectMsgPF(){
      case (userName: String, status: String) =>
        assert(userName == "shruti" && status == "Account Created")
    }
  }

  test("testing username already exists case") {
    databaseActorRef.setAutoPilot((sender: ActorRef, msg: Any) => {
      val result = msg match {
        case (username: String,userDetails: List[String]) => (username, "Username already exists")
      }
      sender ! result
      TestActor.NoAutoPilot
    })
    accountGeneratorActorRef ! List("Shruti","mzn", "shruti", "1000.0")
    expectMsgPF(){
      case (userName: String, status: String) =>
        assert(userName == "shruti" && status == "Username already exists")
    }
  }

  test("testing invalid input case") {
    accountGeneratorActorRef ! 12
    expectMsg("Invalid Information")
  }

}
