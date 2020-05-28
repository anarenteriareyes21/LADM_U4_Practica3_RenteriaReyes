package mx.edu.ittepic.ladm_u4_practica3_serviciosms_renteriareyes

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    /*------ REQUEST CODES----------*/
    var siPermisoReceiver = 101
    var siPermisoLectura  = 102
    var siPermisoEnvio    = 103


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), siPermisoReceiver)
        }
        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), siPermisoLectura)
        }
        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            //se otorga la variable siPermiso si el permiso si se otorga
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),siPermisoEnvio)
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == siPermisoEnvio){
            Toast.makeText(this,"SE OTORGO ENVIAR",Toast.LENGTH_SHORT).show()
        }
        if (requestCode == siPermisoLectura){
            Toast.makeText(this,"SE OTORGO LECTURA",Toast.LENGTH_SHORT).show()
        }
        if (requestCode == siPermisoReceiver){
            Toast.makeText(this,"SE OTORGO RECIBIR",Toast.LENGTH_SHORT).show()

        }

    }




}//class
