package service

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import models.Biller
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

import scala.concurrent.ExecutionContext.Implicits.global

class UserAccountServiceTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar{

  val userAccountService = new UserAccountService
  val userAccountServiceRef = TestProbe()

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  test("testing create account method of this service"){
    userAccountServiceRef.setAutoPilot((sender: ActorRef, msg: Any) => {
      val result = msg match {
        case details: List[String] => (details(2), "Account created")
      }
      sender ! result
      TestActor.KeepRunning
    })

    val result = userAccountService.createAccount(List(List("divya", "mzn", "divya", "1000"),
      List("divyadua", "mzn", "dd", "1000")), userAccountServiceRef.ref)
    result map(result => assert(result === List(("divya", "Account created"), ("divya", "Account created"))))
  }

  test("testing link billers method of this service"){
    userAccountServiceRef.setAutoPilot((sender: ActorRef, msg: Any) => {
      val result = msg match {
        case (accountNumber: Long, biller: Biller) => "Biller is successfully linked"
      }
      sender ! result
      TestActor.KeepRunning
    })

    val result = userAccountService.linkBillers(1000, Biller("phone", "PhoneBiller", 1000), userAccountServiceRef.ref)
    result map(result => assert(result === "Biller is successfully linked"))
  }


}
