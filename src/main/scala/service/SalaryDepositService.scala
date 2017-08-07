package service

import actors.{SalaryDepositActor, UserDatabaseActor}
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait SalaryDepositService {

  def depositSalary(accountNumber: Long, accountHolderName: String,
                    salaryAmount: Double, salaryDepositActorRef: ActorRef): Future[String] = {

    implicit val timeout = Timeout(10 seconds)
    val result = (salaryDepositActorRef ? (accountNumber, accountHolderName, salaryAmount)).mapTo[String]
    //println(". . " + result )
    result
  }

}

object Major1 extends App with SalaryDepositService{

  val system = ActorSystem("UserAccountGeneratorSystem")
  val userDatabaseActorRef: ActorRef = system.actorOf(UserDatabaseActor.props)
  val salaryDepositActorRef: ActorRef = system.actorOf(SalaryDepositActor.props(userDatabaseActorRef))

  depositSalary(900, "Divya", 2000.0, salaryDepositActorRef)
}
