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


class UserAccountService {

  implicit val timeout = Timeout(10 seconds)
  val logger: Logger = Logger.getLogger(this.getClass)

  def createAccount(listOfUsers: List[List[String]], ref: ActorRef): Future[List[(String, String)]] = {

    logger.info("Inside Create account method")
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

