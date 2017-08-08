package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import models.Biller

class BillerActor(databaseActorRef: ActorRef) extends Actor with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {

    case (accountNumber: Long, biller: Biller) =>

      biller.billerCategory match {
        case "phone" | "electricity" | "internet" | "food" | "car" =>
          log.info("Forwarding request to Database actor")
          databaseActorRef forward(accountNumber: Long, biller: Biller)

        case _ => log.info("Biller of such category cannot be linked")
          sender() ! "Biller of such category cannot be linked"
      }
    case _ => log.info("Invalid Inputs")
      sender() ! "Invalid Inputs"
  }

}

object BillerActor{

  def props(databaseActorRef: ActorRef): Props = Props(classOf[BillerActor], databaseActorRef)
}

