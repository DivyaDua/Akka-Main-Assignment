package models

import java.text.SimpleDateFormat
import java.util.Calendar

case class Biller(billerCategory: String, billerName: String, accountNumber: Long,
                  transactionDate: String, amount: Double,
                  totalIterations: Int, executedIterations: Int,
                  paidAmount: Double)

object Biller{

  def apply(billerCategory: String, billerName: String, accountNumber: Long): Biller = {

    val dateFormat = new SimpleDateFormat("d-M-y")
    val currentDate = dateFormat.format(Calendar.getInstance().getTime)

    Biller(billerCategory, billerName, accountNumber, currentDate, 0.0, 0, 0, 0.0)

  }

}