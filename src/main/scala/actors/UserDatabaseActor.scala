package actors

import akka.actor.{Actor, ActorLogging, Props}
import models.{Biller, UserAccount, UserDatabase}

class UserDatabaseActor(userDatabase: UserDatabase) extends Actor with ActorLogging{

  var accountNumberValue = 0

  def receive: PartialFunction[Any, Unit] = {

    case (userName: String, userDetails: List[String]) =>
      log.info("Trying to create account")
      if (!userDatabase.userAccountMap.contains(userName)) {
        accountNumberValue += 1
        userDatabase.addAccount(userName, UserAccount(accountNumberValue.toString :: userDetails))
        sender() ! (userName, "Account Created")
      }
      else {
        sender() ! (userName, "Username already exists")
      }

    case (accountNumber: Long, biller: Biller) =>
      log.info("linking biller by using user database method")
      sender() ! userDatabase.linkBillers(accountNumber, biller)


    case (accountNumber: Long, accountHolderName: String, salary: Double) =>
      log.info("Depositing salary")
      sender() ! userDatabase.depositSalary(accountNumber, accountHolderName, salary)

    case accountNumber: Long => sender() ! userDatabase.getLinkedBillers(accountNumber).map(_.billerCategory)

    case (accountNumber: Long, billAmount: Double, billerCategory: String) =>
      sender() ! userDatabase.payBill(accountNumber, billAmount, billerCategory)
      log.info("All bills paid")
  }
}

object UserDatabaseActor{
  def props(userDatabase: UserDatabase): Props = Props(classOf[UserDatabaseActor],userDatabase)//.withDispatcher("my-dispatcher")
}
