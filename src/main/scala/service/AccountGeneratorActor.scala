package service

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import akka.dispatch.{BoundedMessageQueueSemantics, RequiresMessageQueue}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import models.UserAccount

class AccountGeneratorActor extends Actor with ActorLogging with RequiresMessageQueue[BoundedMessageQueueSemantics]{

  var accountNumberValue: Long = 1000

  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[AccountGenerator])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case userDetails: List[String] if UserDatabaseService.checkForUserNameUniqueness(userDetails(2)) =>
      router.route((userDetails, accountNumberValue), sender())
      accountNumberValue += 1

    case userDetails: List[String] if !UserDatabaseService.checkForUserNameUniqueness(userDetails(2)) =>
      sender()! "User Name already exists"

    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[AccountGenerator])
      context watch r
      router = router.addRoutee(r)
  }

}

class AccountGenerator extends Actor with ActorLogging{

  override def receive = {

    case (userDetails: List[String], accountNumber: Long) =>
      val name = userDetails.head
      val address = userDetails(1)
      val userName = userDetails(2)
      val initialAmount = userDetails(3).toDouble
      val userAccount = UserAccount(accountNumber, name, address, userName, initialAmount)

      UserDatabaseService.addAccount(userName, userAccount)
      sender() ! userAccount

    case _ => log.info("Accounts cannot be created, Undefined for such values")
  }
}

object AccountGeneratorActor{
  def props: Props = Props[AccountGeneratorActor]
}
