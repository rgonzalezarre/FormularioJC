package com.insannity.formulario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.insannity.formulario.ui.theme.ColorFields
import com.insannity.formulario.ui.theme.ColorNeutral
import com.insannity.formulario.ui.theme.ColorNeutralLight
import com.insannity.formulario.ui.theme.ColorPrimary
import com.insannity.formulario.ui.theme.ColorSecondary
import com.insannity.formulario.ui.theme.ColorTertiary
import com.insannity.formulario.ui.theme.ColorTextFields
import com.insannity.formulario.ui.theme.MyBackgroundColor
import com.insannity.formulario.ui.theme.FormularioTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController: NavHostController = rememberNavController()
            var nombre by rememberSaveable {
                mutableStateOf("")
            }
            var apellido by rememberSaveable {
                mutableStateOf("")
            }
            var fechaNac by rememberSaveable {
                mutableStateOf("")
            }
            var genero by rememberSaveable {
                mutableStateOf("")
            }
            var numTel by rememberSaveable {
                mutableStateOf("")
            }
            var email by rememberSaveable {
                mutableStateOf("")
            }
            var interesesSeleccionados by remember {
                mutableStateOf(setOf<String>())
            }
            var descripcion by rememberSaveable {
                mutableStateOf("")
            }
            //Variable para habilitar el boton
            var enableButton by rememberSaveable {
                mutableStateOf(false)
            }
            //variables para el datePicker y validaciones de edad
            var showDatePicker by remember { mutableStateOf(false) }
            val datePickerState = rememberDatePickerState(
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis <= System.currentTimeMillis()
                    }
                }
            )
            //validacion de edad
            var tipoErrorFecha by rememberSaveable { mutableIntStateOf(0) }
            fun validarEdad(fechaMillis: Long?): Int {
                if (fechaMillis == null) return 1
                val hoy = java.util.Calendar.getInstance()
                val nacimiento = java.util.Calendar.getInstance().apply { timeInMillis = fechaMillis }

                if (nacimiento.after(hoy)) return 1

                var edad = hoy.get(java.util.Calendar.YEAR) - nacimiento.get(java.util.Calendar.YEAR)
                if (hoy.get(java.util.Calendar.DAY_OF_YEAR) < nacimiento.get(java.util.Calendar.DAY_OF_YEAR)) edad--

                return if (edad >= 13) 0 else 2
            }

            //Varaible de la lista de generos
            val opcionesGenero = listOf("Masculino", "Femenino", "Otro")
            //Variable lista de intereses
            val opcionesIntereses1 = listOf("Musica","Viajar","Tecnologia")
            val opcionesIntereses2 = listOf("Deportes","Leer","Gaming")
            //Mensajes de error
            //Nombre
            var nombreError by rememberSaveable { mutableStateOf(false) }
            //Apellido
            var apellidoError by rememberSaveable { mutableStateOf(false) }
            //FechaNac
            val mensajeErrorNac = when (tipoErrorFecha) {
                1 -> "Por favor, ingresa una fecha válida"
                2 -> "Debes tener al menos 13 años para registrarte"
                else -> ""
            }

            FormularioTheme {
                NavHost(
                    navController = navController,
                    startDestination = MainScreenDestination
                ){
                    composable<MainScreenDestination> {
                        Scaffold(
                            containerColor = MyBackgroundColor,
                            topBar = {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MyBackgroundColor),
                                    title= {
                                        Text(
                                            text = "Completa tu perfil",
                                            fontSize = 20.sp,
                                            fontFamily =FontFamily(Font(R.font.plusjakartabold)),
                                            color = ColorSecondary
                                        )
                                    },
                                    actions = {
                                        Box(modifier= Modifier.padding(end = 20.dp)){
                                            Text(
                                                text = "Form",
                                                fontSize = 20.sp,
                                                fontFamily =FontFamily(Font(R.font.plusjakartabold)),
                                                color= ColorPrimary

                                            )
                                        }


                                    }
                                )
                            }
                        ){innerPadding ->
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)      // Es para el espacio del TopBar y barra de estado
                                    .padding(horizontal = 40.dp)
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                //Parte de crea tu perfil y texto inicial
                                Text(
                                    text = "Crea tu perfil",
                                    fontSize = 32.sp,
                                    fontFamily = FontFamily(Font(R.font.plusjakartabold)),
                                    color = ColorNeutralLight,
                                    modifier = Modifier.padding(top = 40.dp)
                                )
                                Text(
                                    text = "Permite a los demas conocer mas acerca de ti antes de continuar.",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    color = ColorNeutral,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Imagen
                                Box(
                                    modifier = Modifier
                                        .size(150.dp) // Tamaño de la foto de perfil
                                        .align(Alignment.CenterHorizontally) // Centra el conjunto en la columna
                                ) {
                                    // Donde va la foto
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(ColorFields, shape = RoundedCornerShape(75.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = null,
                                            modifier = Modifier.size(50.dp),
                                            tint = ColorNeutral
                                        )
                                    }

                                    //Icono de camara
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .align(Alignment.BottomEnd) // Lo posiciona abajo a la derecha
                                            .background(ColorTertiary, shape = RoundedCornerShape(24.dp))
                                            .clickable { /* Aquí abrirías la galería */ },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ExitToApp, // Me falta poner el icono
                                            contentDescription = "Cambiar foto",
                                            modifier = Modifier.size(18.dp),
                                            tint = ColorNeutralLight
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(32.dp))
                                //Nombre
                                Text(
                                    text = "Nombre(s)",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                TextField(
                                    value = nombre,
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text
                                    ),
                                    onValueChange = { nuevoValor ->
                                        if(nuevoValor.length<20) {
                                            nombre = nuevoValor
                                            nombreError=nombre.isEmpty()
                                        }

                                    },
                                    placeholder = {
                                        Text(
                                            text = "Tu nombre",
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = ColorFields,
                                        focusedContainerColor = ColorFields,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = ColorTextFields,//Color de cuando se cambia el texto
                                        unfocusedTextColor= ColorTextFields,
                                        cursorColor = ColorTextFields
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    //isError=nombreError,//Es para marcar la casiila de error
                                    supportingText = {
                                        if(nombreError){
                                            Text(
                                                text="Nombre obligatorio",
                                                color=Color.Red
                                            )
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Apellido
                                Text(
                                    text = "Apellido",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                TextField(
                                    value = apellido,
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text
                                    ),
                                    onValueChange = { nuevoValor ->
                                        if(apellido.length<20){
                                            apellido = nuevoValor
                                            apellidoError=apellido.isEmpty()
                                        }
                                    },
                                    supportingText = {
                                        if(apellidoError){
                                            Text(
                                                text="Apellido obligatorio",
                                                color=Color.Red
                                            )
                                        }

                                    },
                                    placeholder = {
                                        Text(
                                            text = "Tu apellido",
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = ColorFields,
                                        focusedContainerColor = ColorFields,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = ColorTextFields,//Color de cuando se cambia el texto
                                        unfocusedTextColor= ColorTextFields,
                                        cursorColor=ColorTextFields
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Fecha de nacimiento
                                Text(
                                    text = "Fecha de nacimiento",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .clickable { showDatePicker = true } // <--- EL CLIC VA AQUÍ
                                ) {
                                    TextField(
                                        modifier=Modifier.fillMaxWidth(),
                                        value = fechaNac,
                                        onValueChange = { },
                                        readOnly = true,   // Importante para que no salga el teclado
                                        enabled = false,    // Importante para que el clic pase al Box
                                        placeholder = {
                                            Text(
                                                text = "dd/mm/aaaa",
                                                color = ColorNeutral,
                                                fontSize = 14.sp
                                            )
                                        },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = TextFieldDefaults.colors(
                                            disabledContainerColor = ColorFields,
                                            disabledTextColor = ColorTextFields,
                                            disabledIndicatorColor = Color.Transparent,
                                            disabledPlaceholderColor = ColorTextFields
                                        ),
                                        supportingText = {
                                            if (tipoErrorFecha != 0) {
                                                Text(
                                                    text = mensajeErrorNac,
                                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                                    color = Color.Red
                                                )
                                            }
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.size(32.dp))
                                //Genero
                                Text(
                                    text = "Genero",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                Row(modifier=Modifier.fillMaxWidth()){
                                    //Iteramos las opciones para crear los botones
                                    opcionesGenero.forEach { texto ->

                                        RadioButton(
                                            selected = (texto == genero), // ¿Está marcado?
                                            onClick = { genero = texto }, // Al hacer clic
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = ColorTertiary,
                                                unselectedColor = ColorNeutral
                                            )
                                        )
                                        Text(
                                            text = texto,
                                            color = ColorTextFields,
                                            modifier = Modifier
                                                .padding(top = 15.dp),
                                            fontSize= 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                }


                                }
                                Spacer(modifier = Modifier.size(32.dp))
                                //Numero de telefono, todavia me falta hacer que maneje el numero bien
                                Text(
                                    text = "Número de teléfono",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                TextField(
                                    value = numTel,
                                    modifier = Modifier.fillMaxWidth(),
                                    onValueChange = { nuevoValor ->
                                        numTel = nuevoValor
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Tu numero",
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = ColorFields,
                                        focusedContainerColor = ColorFields,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = ColorTextFields//Color de cuando se cambia el texto
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Correo electronico
                                Text(
                                    text = "Correo electrónico",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    color = ColorNeutral
                                )
                                TextField(
                                    value = email,
                                    onValueChange = { newValue->
                                        if(newValue.length<40)
                                        email = newValue },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    placeholder = {
                                        Text(
                                            text="ejemplo@correo.com",
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))

                                        )
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Email    // Muestra el '@' y el '.com' en el teclado
                                    ),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = ColorFields,
                                        unfocusedContainerColor = ColorFields,
                                        focusedTextColor = ColorTextFields,
                                        unfocusedTextColor = ColorTextFields,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    singleLine = true // Importante para que no crezca hacia abajo
                                )

                                Spacer(modifier = Modifier.size(32.dp))
                                //Intereses personales 1
                                Text(
                                    text = "Intereses personales",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    color = ColorNeutral
                                )
                                Spacer(modifier = Modifier.size(2.dp))
                                Row {
                                    opcionesIntereses1.forEach { texto->
                                        val estaSeleccionado = interesesSeleccionados.contains(texto)
                                        Button(
                                            onClick = {
                                                interesesSeleccionados = if (estaSeleccionado) {
                                                    interesesSeleccionados - texto
                                                } else {
                                                    interesesSeleccionados + texto
                                                }
                                            },
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = if (estaSeleccionado) Color.Transparent else ColorPrimary
                                            ),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (estaSeleccionado) ColorPrimary else MyBackgroundColor,
                                                contentColor = if (estaSeleccionado) MyBackgroundColor else ColorNeutralLight
                                            ),
                                            modifier = Modifier
                                                .padding(start = 2.dp)
                                                .weight(1f)// Para que se repartan el espacio equitativamente
                                        ) { Text(
                                            text=texto,
                                            fontSize = 12.sp,
                                            fontFamily= FontFamily(Font(R.font.manrope))
                                        )}
                                    }
                                }
                                Spacer(modifier = Modifier.size(1.dp))
                                //Intereses personales 2
                                Row{

                                    opcionesIntereses2.forEach { texto->
                                        val estaSeleccionado = interesesSeleccionados.contains(texto)
                                        Button(
                                            onClick = {
                                                interesesSeleccionados = if (estaSeleccionado) {
                                                    interesesSeleccionados - texto
                                                } else {
                                                    interesesSeleccionados + texto
                                                }
                                            },
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = if (estaSeleccionado) Color.Transparent else ColorPrimary
                                            ),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (estaSeleccionado) ColorPrimary else MyBackgroundColor,
                                                contentColor = if (estaSeleccionado) MyBackgroundColor else ColorNeutralLight
                                            ),
                                            modifier = Modifier
                                                .padding(start = 4.dp)
                                                .weight(1f)// Para que se repartan el espacio equitativamente
                                        ) { Text(
                                            text=texto,
                                            fontSize = 12.sp,
                                            fontFamily= FontFamily(Font(R.font.manrope))
                                        )}
                                    }
                                }
                                Spacer(modifier = Modifier.size(32.dp))
                                //Descripcion publica del perfil
                                Text(
                                    text = "Descripcion publica del perfil (MAX. 250)",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    color = ColorNeutral
                                )
                                TextField(
                                    value = descripcion,
                                    modifier = Modifier.fillMaxWidth(),
                                    onValueChange = { nuevoValor ->
                                        if(nuevoValor.length<= 250)
                                        descripcion = nuevoValor
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Cuentanos un poco sobre ti...",
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = ColorFields,
                                        focusedContainerColor = ColorFields,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = ColorTextFields//Color de cuando se cambia el texto
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Boton de registro
                                Row(
                                    modifier=Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center){
                                    Button(
                                        onClick = {
                                            navController.navigate(
                                                DataScreenDestination(
                                                    name = nombre,
                                                    apellido = apellido,
                                                    fechaNac = fechaNac,
                                                    genero= genero,
                                                    numTel=numTel,
                                                    email=email,
                                                    intereses= interesesSeleccionados.toList(),
                                                    descripcion= descripcion
                                                )
                                            )
                                        },
                                        colors=ButtonDefaults.buttonColors(
                                            //containerColor = if (estaSeleccionado) ColorPrimary else MyBackgroundColor,
                                            // contentColor = if (estaSeleccionado) Color.Black else ColorNeutralLight
                                            containerColor = ColorPrimary,
                                            contentColor = MyBackgroundColor
                                        ),
                                        enabled = if(nombre.isNotEmpty() && apellido.isNotEmpty() && fechaNac.isNotEmpty() && genero.isNotEmpty() && numTel.isNotEmpty() && email.isNotEmpty()){
                                            true
                                        }else{
                                            false
                                        }
                                    ) {
                                        Text(
                                            text = "Terminar perfil",
                                            fontSize = 32.sp,
                                            fontFamily = FontFamily(Font(R.font.plusjakartabold))
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(100.dp))
                                }

                            }
                            if (showDatePicker) {
                                DatePickerDialog(
                                    onDismissRequest = { showDatePicker = false },
                                    confirmButton = {
                                        Button(onClick = {
                                            val resultado = validarEdad(datePickerState.selectedDateMillis)
                                            tipoErrorFecha = resultado

                                            if (resultado == 0) {
                                                fechaNac = datePickerState.selectedDateMillis?.let {
                                                    java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                                        .format(java.util.Date(it))
                                                } ?: ""
                                            } else {
                                                fechaNac = "" // Limpiamos si hay error
                                            }
                                            showDatePicker = false
                                        }) { Text("OK") }
                                    }
                                ) {
                                    DatePicker(state = datePickerState)
                                }
                            }
                        }
                    }
                    composable<DataScreenDestination>{backStackEtry ->
                        val args=backStackEtry.toRoute<DataScreenDestination>()
                        Scaffold(
                            containerColor = MyBackgroundColor,
                            topBar = {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MyBackgroundColor),
                                    title= {
                                        Text(
                                            text = "Perfiles",
                                            fontSize = 20.sp,
                                            fontFamily =FontFamily(Font(R.font.plusjakartabold)),
                                            color = ColorSecondary
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = {
                                                navController.popBackStack()
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Icono de flecha de regreso",
                                                tint = ColorSecondary
                                            )
                                        }
                                    },
                                    actions = {
                                        Box(modifier= Modifier.padding(end = 20.dp)){
                                            Text(
                                                text = "Form",
                                                fontSize = 20.sp,
                                                fontFamily =FontFamily(Font(R.font.plusjakartabold)),
                                                color= ColorPrimary

                                            )
                                        }


                                    }
                                )
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)      // Es para el espacio del TopBar y barra de estado
                                    .padding(horizontal = 30.dp)
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ){
                                Text(
                                    text=args.name,
                                    color= ColorNeutralLight
                                )
                            }

                        }
                    }

                    }
                }
            }
        }
    }

@Serializable
object MainScreenDestination

@Serializable
data class DataScreenDestination(
    val name: String,
    val apellido: String,
    val fechaNac: String,
    val genero: String,
    val numTel: String,
    val email: String,
    val intereses: List<String>,
    val descripcion: String

)



