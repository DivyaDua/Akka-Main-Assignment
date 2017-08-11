package service

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import org.scalatest.mockito.MockitoSugar
import scala.concurrent.ExecutionContext.Implicits.global

class SalaryDepositServiceTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar{

  val salaryDepositActorRef = TestProbe()
  val salaryDepositService = new SalaryDepositService

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  test("testing deposit salary method of this service"){
    salaryDepositActorRef.setAutoPilot((sender: ActorRef, msg: Any) => {
      val result = msg match {
        case (accountNumber: Long, accountHolderName: String,
        salaryAmount: Double) => true
      }
      sender ! result
      TestActor.KeepRunning
    })

    val result = salaryDepositService.depositSalary(1000L, "Divya", 1000.0, salaryDepositActorRef.ref)
    result map(result => assert(result === true))
  }


}
