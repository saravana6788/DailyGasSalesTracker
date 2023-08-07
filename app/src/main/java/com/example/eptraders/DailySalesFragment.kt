package com.example.eptraders

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.eptraders.databinding.FragmentSecondBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DailySalesFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        //binding.todayDate.text = "Sale On ${getYesterdayDate()}"

        binding.saleDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                   val formattedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month+1, year)
                    binding.saleDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }



        binding.sendButton.setOnClickListener {
            sendMessageViaWhatsapp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessageViaWhatsapp(){
            val saleDate = binding.saleDate.text
            val msSalesInLitres = binding.petrolSales.text
            val hsdSalesInLitres = binding.dieselSales.text
            val msCumulativeSalesInLitres = binding.cumulativePetrolSales.text
            val hsdCumulativeSalesInLitres = binding.cumulativeDieselSales.text
            val msTodayPurchase = binding.petrolPurchase.text
            val hsdTodayPurchase = binding.dieselPurchase.text
            val msAvailableStock = binding.inStockPetrol.text
            val hsdAvailableStock = binding.inStockDiesel.text
            val msRate = binding.petrolPrice.text
            val hsdRate = binding.dieselPrice.text


        val message = "\t\tSale On *${saleDate}*\t\t\n" +
                "\n" +
                "*Rate:*\t\t\n" +
                "\n" +
                "MS : ${msRate}\t\tHSD : ${hsdRate}\n" +
                "\n" +
                "*Sales in Litres:*\n" +
                "\n" +
                "MS : ${msSalesInLitres}\t\tHSD : ${hsdSalesInLitres}\n" +
                "\n" +
                "*Cumulative Sales in Litres:*\n" +
                "\n" +
                "MS : ${msCumulativeSalesInLitres}\t\tHSD : ${hsdCumulativeSalesInLitres}\n" +
                "\n" +
                "*Purchase in Litres:*\n" +
                "\n" +
                "MS : ${msTodayPurchase}\t\tHSD : ${hsdTodayPurchase}\n" +
                "\n" +
                "*Total Available Stock:*\n" +
                "\n" +
                "MS : ${msAvailableStock}\t\tHSD : ${hsdAvailableStock}\n\n"




        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getYesterdayDate(): String {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return yesterday.format(formatter)
    }
}