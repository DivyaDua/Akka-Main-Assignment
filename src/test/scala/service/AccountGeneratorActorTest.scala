package service

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

class AccountGeneratorActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar{

  /*val userDatabaseService: UserDatabaseService = mock[UserDatabaseService]
  val ref: ActorRef = system.actorOf(AccountGeneratorActor.props(userDatabaseService))

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  test("testing account generator actor"){

    when(userDatabaseService.checkForUserNameUniqueness("prince")) thenReturn true
    ref ! List("Prince Dua", "mzn", "prince", "1000")

    expectMsgPF(){
      case (username: String, result: String) =>
        assert(username == "prince" &&  result == "Account Created")
    }
  }

  test("testing account generator actor with already existing username"){

    when(userDatabaseService.checkForUserNameUniqueness("prince")) thenReturn false
    ref ! List("Prince Dua", "mzn", "prince", "1000")

    expectMsgPF(){
      case (username: String, result: String) =>
        assert(username == "prince" &&  result == "Username already exists")
    }
  }
*/
  /*test("testing account generator actor with invalid data"){
    ref ! List(1, 2, 3, 4)

    expectMsgPF(){
      case (username: String, result: String) =>
        assert(username == "Invalid Name" &&  result == "Invalid Information")
    }
  }*/

}
