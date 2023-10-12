package com.example.monetario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txt_result)

        val buttonConverter = findViewById<Button>(R.id.btn_converter)

        buttonConverter.setOnClickListener {
            converter()
        }
    }

    private fun converter() {
        val selectedCurrency = findViewById<RadioGroup>(R.id.radio_group)

        val checked = selectedCurrency.checkedRadioButtonId

        val currency = when(checked) {
            R.id.radio_usd -> "USD"
            R.id.radio_eur -> "EUR"
            else           -> "CLP"
        }
        val editField = findViewById<EditText>(R.id.edit_field)

        val value = editField.text.toString()

        if (value.isEmpty())
            return

        result.text = value
        result.visibility = View.VISIBLE

        Thread {
            // aqui acontece em paralelo

            val url = URL("https://free.currconv.com/api/v7/convert?q=${currency}_BRL&compact=ultra&apiKey=8bd0f9b4ed8e2cc49c9c")

            val conn = url.openConnection() as HttpsURLConnection

            try {

                val data = conn.inputStream.bufferedReader().readText()

                // {"adasdajoajad": 121121313}

                val obj = JSONObject(data)
                runOnUiThread {
                    val res = obj.getDouble("${currency}_BRL")

                    result.text = "R$${"%.4f".format(value.toDouble() * res)}"
                    result.visibility = View.VISIBLE
                }

            } finally {
                conn.disconnect()
            }

        }.start()

    }

}


// COMO CRIAR UM APLICATIVO PARA ANDROID DO ZERO | ANDROID STUDIO TUTORIAL PARA INICIANTES #1
// https://www.youtube.com/watch?v=z0gjnpvb8Zg&list=PLJ0AcghBBWShk-phKLJ4qpHqTvZ3W0sUO&index=1

// COMO CRIAR UM APLICATIVO PARA ANDROID DO ZERO | ANDROID STUDIO TUTORIAL PARA INICIANTES #2
// https://www.youtube.com/watch?v=tQuZao2M-34&list=PLJ0AcghBBWShk-phKLJ4qpHqTvZ3W0sUO&index=2


// TODO - falta pegar a apiKey e alterar na URL, para conseguir consumir os dados da conversão de moedas.
// https://free.currconv.com/api/v7/convert?q=USD_BRL&compact=ultra&apiKey=SUA_API_KEY