package service

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.apache.log4j.Logger

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SalaryDepositService {

  val logger: Logger = Logger.getLogger(this.getClass)

  def depositSalary(accountNumber: Long, accountHolderName: String,
                    salaryAmount: Double, salaryDepositActorRef: ActorRef): Future[Boolean] = {

    logger.info("Inside deposit salary method")
    implicit val timeout = Timeout(10 seconds)
    (salaryDepositActorRef ? (accountNumber, accountHolderName, salaryAmount)).mapTo[Boolean]
  }

}

