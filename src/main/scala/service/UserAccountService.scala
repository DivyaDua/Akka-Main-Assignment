package service

import actors.{AccountGeneratorActor, BillerActor, UserDatabaseActor}
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import models.{Biller, UserDatabase}
import org.apache.log4j.Logger

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait UserAccountService {

  implicit val timeout = Timeout(10 seconds)
  val logger: Logger = Logger.getLogger(this.getClass)

  def createAccount(listOfUsers: List[List[String]], ref: ActorRef): Future[List[(String, String)]] = {

    Thread.sleep(4000)
    val accountsListOfFuture = listOfUsers.map(ref ? _).map(_.mapTo[(String, String)])
    Future.sequence(accountsListOfFuture)
  }

  def linkBillers(accountNumber: Long, biller: Biller, billerRef: ActorRef): Future[String] = {

    Thread.sleep(4000)
    logger.info(s"Linking Biller with account $accountNumber")
    (billerRef ? (accountNumber, biller)).mapTo[String]

  }

}

/*
object Major extends App with UserAccountService{

  val userDatabase = new UserDatabase
  val system = ActorSystem("UserAccountGeneratorSystem")
  val userDatabaseActorRef: ActorRef = system.actorOf(UserDatabaseActor.props(userDatabase))
  val accountGenertorActorRef: ActorRef = system.actorOf(AccountGeneratorActor.props(userDatabaseActorRef))
  val billerRef: ActorRef = system.actorOf(BillerActor.props(userDatabaseActorRef))

  createAccount(List(List("divya", "mzn", "dd", "1000"), List("divyadua", "mzn", "dd", "1000"),
    List("divyadua", "mzn", "dd", "1000")), accountGenertorActorRef)

  Thread.sleep(5000)
  print(linkBillers(1000, Biller("phone", "Divya", 1000), billerRef))
}
*/
