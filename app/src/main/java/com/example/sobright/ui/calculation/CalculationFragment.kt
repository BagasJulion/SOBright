package com.example.sobright.ui.calculation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.sobright.R
import java.util.Random

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalculationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calculation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn_hitung1: Button = view.findViewById(R.id.btn_hitung1)
        val edt_tinggi: EditText = view.findViewById(R.id.edt_tinggi)
        val edt_lebar: EditText = view.findViewById(R.id.edt_lebar)
        val textHasil1: TextView = view.findViewById(R.id.textHasil1)

        val btn_hitung2: Button = view.findViewById(R.id.btn_hitung2)
        val tv_energy: TextView = view.findViewById(R.id.tv_energy)
        val tv_harga: TextView = view.findViewById(R.id.tv_harga)
        val textHasil2: TextView = view.findViewById(R.id.textHasil2)

        btn_hitung1.setOnClickListener {
            val tinggi = edt_tinggi.text.toString()
            val lebar = edt_lebar.text.toString()

            if (tinggi.isBlank()) {
                edt_tinggi.error = "Height cannot be empty"
                edt_tinggi.requestFocus()
            } else if (lebar.isBlank()) {
                edt_lebar.error = "Width cannot be empty"
                edt_lebar.requestFocus()
            } else {
                hitungLuas(tinggi.toInt(), lebar.toInt(), textHasil1)
            }
        }

        btn_hitung2.setOnClickListener {
            val (dummyEnergy, dummyHarga) = createDummyData()
            tv_energy.text = dummyEnergy
            tv_harga.text = dummyHarga

            val energy = tv_energy.text.toString()
            val harga = tv_harga.text.toString()

            if (energy.isBlank()) {
                tv_energy.error = "Energy cannot be empty"
                tv_energy.requestFocus()
            } else if (harga.isBlank()) {
                tv_harga.error = "Price cannot be empty"
                tv_harga.requestFocus()
            } else {
                hitungEnergyHarga(energy.toInt(), 1500, textHasil2)
            }
        }
    }
    //dummy data
    private fun createDummyData(): Pair<String, String> {
            val random = Random()
            val dummyEnergy = random.nextInt(500).toString()
            val dummyHarga = "1500"
            return Pair(dummyEnergy, dummyHarga)
    }

    private fun hitungLuas(tinggi: Int, lebar: Int, textHasil: TextView) {
        val hasil = tinggi * lebar
        textHasil.text = hasil.toString()
    }

    private fun hitungEnergyHarga(energy: Int, harga: Int, textHasil: TextView) {
        val hasil = energy * harga
        textHasil.text = hasil.toString()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalculationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}