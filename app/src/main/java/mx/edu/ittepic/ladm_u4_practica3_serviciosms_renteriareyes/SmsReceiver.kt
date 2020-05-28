package mx.edu.ittepic.ladm_u4_practica3_serviciosms_renteriareyes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Console

class SmsReceiver  : BroadcastReceiver () {

    var baseRemota = FirebaseFirestore.getInstance()

    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent!!.extras
        if (extras != null) {
            var sms = extras.get("pdus") as Array<Any> //obtener los mensajes
            for (indice in sms.indices) {
                var formato = extras.getString("format")
                //version de sistema operativo
                var smsMensaje = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(sms[indice] as ByteArray, formato)
                } else {
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }
                var celularOrigen = smsMensaje.originatingAddress //celular de origen
                var contenidoSMS = smsMensaje.messageBody.toString() //mensaje entrante
                analizarMensaje(contenidoSMS.toUpperCase(),celularOrigen!!, context)


            }

        }
    }
    /*----------------------------- ANALIZAR EL CUERPO DEL MENSAJE---------------------*/
    fun analizarMensaje( msn : String, cel : String , context: Context?){
        var listaModos = ArrayList<String>()
        var encontrado = false

        listaModos.add("FELIZ")
        listaModos.add("TRISTE")
        listaModos.add("FIT")
        listaModos.add("FRESA")
        listaModos.add("ROMANTICO")
        listaModos.add("OLDIE")
        listaModos.add("CHILLOUT")
        listaModos.add("ROCKERO")
        listaModos.add("BADASS")
        var cuerpo = msn.split(" ").toMutableList()
        //Toast.makeText(context,"ANTES: ${cuerpo.size}",Toast.LENGTH_SHORT).show()
        /*------------- ELIMINAR ULTIMO ESPACIO EN BLANCO ------------------*/
        if (cuerpo[cuerpo.lastIndex] == ""){
            Toast.makeText(context,"ULTIMO BLANCO",Toast.LENGTH_SHORT).show()
            cuerpo.removeAt(cuerpo.lastIndex)
        }
        //Toast.makeText(context,"DESPUES: ${cuerpo.size}",Toast.LENGTH_SHORT).show()
        /*----------------------------------- VALIDAR LA CADENA ------------------------*/
        if (cuerpo.size < 2  || cuerpo.size > 2){
            //EL MENSAJE TIENE MENOS/MAS DE LAS PALABRAS NECESARIAS-> NO SE REVISA
            enviarMensajeError(cel)
        }else {
           // Toast.makeText(context,"CANTIDAD CORRECTA",Toast.LENGTH_SHORT).show()
            if (cuerpo[0] == "MOOD") {
                if (listaModos.contains(cuerpo[1])){
                   // Toast.makeText(context,"EL MOOD SI ESTA",Toast.LENGTH_SHORT).show()
                    enviarMensaje(cuerpo[1], cel, context)
                }else{
                    //Error la sintaxis del mensaje
                    enviarMensajeError(cel)
                   // Toast.makeText(context,"ERROR EL MOOD NO ESTA",Toast.LENGTH_SHORT).show()
                }
            } else {
                //Error la sintaxis del mensaje
                enviarMensajeError(cel)
            }
        }
        cuerpo.clear()
    }


    fun enviarMensaje(mood : String , celular : String, context: Context?){
        baseRemota.collection("moody").whereEqualTo("tipo",mood)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                var nomcancion = ""
                var artista = ""
                var link = ""
                for(document in querySnapshot!!){
                    nomcancion = document.getString("nombre_cancion").toString()
                    artista = document.getString("artista").toString()
                    link = document.getString("link").toString()
                }
               //Toast.makeText(context,"${artista}",Toast.LENGTH_SHORT).show()
                SmsManager.getDefault().sendTextMessage(celular,null,"MOOD ${mood} CON: ${nomcancion} - ${artista} Escuchala en: ${link}",null,null)
                Toast.makeText(context,"Se envio el mensaje",Toast.LENGTH_SHORT).show()
            }
    }

    fun enviarMensajeError(celular: String){
        SmsManager.getDefault() 
            .sendTextMessage(celular, null,"ERROR, SI QUIERES UNA CANCION PARA TU MOOD DEBES ESCRIBIR: MOOD tuMood (Feliz, Triste,Fit, etc.)" , null, null)
    }

}//class