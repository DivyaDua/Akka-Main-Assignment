package actors

import akka.actor.{Actor, ActorLogging, Props}
import models.{Biller, UserAccount, UserDatabase}

class UserDatabaseActor extends UserDatabase with Actor with ActorLogging{

  var accountNumberValue = 0

  def receive: PartialFunction[Any, Unit] = {

    case (userName: String, userDetails: List[String]) =>
      log.info("Trying to create account")
      if (!userAccountMap.contains(userName)) {
        userAccountMap += (userName -> UserAccount(accountNumberValue.toString :: userDetails))
        accountNumberValue += 1
        sender() ! (userName, "Account Created")
      }
      else
        sender() ! (userName, "Username already exists")

    case (accountNumber: Long, biller: Biller) =>
      linkBillers(accountNumber, biller)
      log.info("Biller Linked")
      sender() ! "Task of linking is done"

    case (accountNumber: Long, accountHolderName: String, salary: Double) =>
      depositSalary(accountNumber, accountHolderName, salary)
      log.info("Salary Deposited")

    case accountNumber: Long => sender() ! getLinkedBillers(accountNumber).map(_.billerCategory)

    case (accountNumber: Long, billAmount: Double, billerCategory: String) =>
      sender() ! payBill(accountNumber, billerCategory, billAmount)
      log.info("All bills paid")
  }
}

object UserDatabaseActor{
  def props: Props = Props(classOf[UserDatabaseActor])//.withDispatcher("my-dispatcher")
}
