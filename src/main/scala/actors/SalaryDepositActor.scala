package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

class SalaryDepositActor(userDatabaseActorRef: ActorRef) extends Actor with ActorLogging{

  override def receive: PartialFunction[Any, Unit] = {

    case (accountNumber: Long, accountHolderName: String, salary: Double) =>
      log.info("Forwarding salary deposit request to Database Actor")
      userDatabaseActorRef forward(accountNumber, accountHolderName, salary)
      implicit val timeout = Timeout(10 seconds)

      val listOfBillers = (userDatabaseActorRef ? accountNumber).mapTo[mutable.ListBuffer[String]]

      listOfBillers.onComplete{
        case Success(list) => log.info("Received list of billers")
          list.foreach(billerCategory => context.actorOf(BillProcessingActor.props(userDatabaseActorRef)).forward(accountNumber, billerCategory))

        case Failure(ex) => log.info("Failed while receiving list Of Billers with exception " + ex)
      }

    case _ => log.info("Invalid inputs")
      sender() ! "Invalid values"
  }

}

class BillProcessingActor(databaseServiceActorRef: ActorRef) extends Actor with ActorLogging {

  val CAR_BILL: Double = 100
  val PHONE_BILL: Double = 200
  val INTERNET_BILL: Double = 300
  val ELECTRICITY_BILL: Double = 400
  val FOOD_BILL: Double = 500

  override def receive: PartialFunction[Any, Unit] = {

    case (accountNo: Long, billerCategory: String) =>
      log.info("within bill processing actor")
      billerCategory match {

        case "car" => databaseServiceActorRef.forward(accountNo, CAR_BILL, "car")
        case "phone" => databaseServiceActorRef.forward(accountNo, PHONE_BILL, "phone")
        case "internet" => databaseServiceActorRef.forward(accountNo, INTERNET_BILL, "internet")
        case "electricity" => databaseServiceActorRef.forward(accountNo, ELECTRICITY_BILL, "electricity")
        case "food" => databaseServiceActorRef.forward(accountNo, FOOD_BILL, "food")

      }
    case _ => sender() ! "Invalid Inputs"
  }

}

object BillProcessingActor {

  def props(userDatabaseActorRef: ActorRef): Props = Props(classOf[BillProcessingActor], userDatabaseActorRef)

}
object SalaryDepositActor{

  def props(userDatabaseActorRef: ActorRef): Props = Props(classOf[SalaryDepositActor], userDatabaseActorRef)
}
