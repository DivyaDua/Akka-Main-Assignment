import actors.{AccountGeneratorActor, BillerActor, SalaryDepositActor, UserDatabaseActor}
import akka.actor.{ActorRef, ActorSystem}
import models.{Biller, UserDatabase}
import org.apache.log4j.Logger
import service.{SalaryDepositService, UserAccountService}

object MainClass extends App{

  val logger: Logger = Logger.getLogger(this.getClass)

  val salaryDepositService = new SalaryDepositService
  val userAccountService = new UserAccountService

  val userDatabase = new UserDatabase
  val system = ActorSystem("UserAccountGeneratorSystem")
  val userDatabaseActorRef: ActorRef = system.actorOf(UserDatabaseActor.props(userDatabase))
  val accountGeneratorActorRef: ActorRef = system.actorOf(AccountGeneratorActor.props(userDatabaseActorRef))
  val billerRef: ActorRef = system.actorOf(BillerActor.props(userDatabaseActorRef))
  val salaryDepositActorRef: ActorRef = system.actorOf(SalaryDepositActor.props(userDatabaseActorRef))

  logger.info("Executing create account method")
  userAccountService.createAccount(List(List("divya", "mzn", "dd", "1000"), List("divyadua", "mzn", "dd", "1000"),
    List("divyadua", "mzn", "dd", "1000")), accountGeneratorActorRef)

  logger.info("Executing link billers method")
  userAccountService.linkBillers(1000, Biller("phone", "PhoneBiller", 1000), billerRef)

  logger.info("Executing deposit salary method")
  salaryDepositService.depositSalary(900, "Divya", 2000.0, salaryDepositActorRef)
}
