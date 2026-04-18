package com.insannity.formulario

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.insannity.formulario.ui.theme.ColorError
import com.insannity.formulario.ui.theme.ColorFields
import com.insannity.formulario.ui.theme.ColorNeutral
import com.insannity.formulario.ui.theme.ColorNeutralLight
import com.insannity.formulario.ui.theme.ColorPrimary
import com.insannity.formulario.ui.theme.ColorSecondary
import com.insannity.formulario.ui.theme.ColorTertiary
import com.insannity.formulario.ui.theme.ColorTextFields
import com.insannity.formulario.ui.theme.MyBackgroundColor
import com.insannity.formulario.ui.theme.FormularioTheme
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import kotlinx.serialization.Serializable
import java.util.Calendar

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val focusManager = LocalFocusManager.current
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
            //varaibles para lo del numero de telefono y la lada
            var phoneNumber by rememberSaveable { mutableStateOf("") }
            val countryPickerState = rememberKomposeCountryCodePickerState(
                defaultCountryCode="mx",
                showCountryCode = true,
                showCountryFlag = true,
            )
            var generoError by rememberSaveable { mutableStateOf(false) }

            var phoneError by rememberSaveable { mutableStateOf("0") }

            var email by rememberSaveable {
                mutableStateOf("")
            }
            var interesesSeleccionados by remember {
                mutableStateOf(setOf<String>())
            }
            var descripcion by rememberSaveable {
                mutableStateOf("")
            }

            //variables para el calendario
            var fechaNacError by rememberSaveable { mutableStateOf("0") }
            val context = LocalContext.current
            val calendar = Calendar.getInstance()
            val calendarLimite = Calendar.getInstance()
            calendarLimite.add(Calendar.YEAR, -13)
            val datePickerDialog = android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    fechaNac = "$dayOfMonth/${month + 1}/$year"

                    // --- LÓGICA DE VALIDACIÓN ---
                    val hoy = Calendar.getInstance()
                    val fechaSeleccionada = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }

                    // Calculamos la edad
                    var edad = hoy.get(Calendar.YEAR) - fechaSeleccionada.get(Calendar.YEAR)

                    // Ajuste por si aún no ha pasado su cumpleaños este año
                    if (hoy.get(Calendar.DAY_OF_YEAR) < fechaSeleccionada.get(Calendar.DAY_OF_YEAR)) {
                        edad--
                    }

                    // Si es menor de 13, marcamos error
                    fechaNacError = if(edad < 13){
                        "1"
                    } else "0"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                // Esto permite seleccionar cualquier fecha hasta el día de hoy
                datePicker.maxDate = System.currentTimeMillis()
            }




            //Varaible de la lista de generos
            val opcionesGenero = listOf(stringResource(R.string.male),
                stringResource(R.string.female), stringResource(R.string.other)
            )
            //Variable lista de intereses
            val opcionesIntereses1 = listOf(stringResource(R.string.music),
                stringResource(R.string.travel), stringResource(R.string.technology)
            )
            val opcionesIntereses2 = listOf(stringResource(R.string.sports),
                stringResource(R.string.read), stringResource(R.string.gaming)
            )

            //Validaciones de correo
            // Estado para controlar si el email es válido
            var emailError by rememberSaveable { mutableStateOf("0") }
            // Patrón estándar de validación de correo
            val emailPattern = Patterns.EMAIL_ADDRESS

            //Mensajes de error
            //Nombre
            var nombreError by rememberSaveable { mutableStateOf(false) }
            //Apellido
            var apellidoError by rememberSaveable { mutableStateOf(false) }
            //FechaNac
            //Variable para boton
            val esFormularioValido = nombre.isNotEmpty() && !nombreError &&
                    apellido.isNotEmpty() && !apellidoError &&
                    fechaNac.isNotEmpty() && fechaNacError == "0" &&
                    genero.isNotEmpty() &&
                    phoneNumber.length == 10 && phoneError == "0" &&
                    email.isNotEmpty() && emailError == "0"

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
                                            text = stringResource(R.string.completa_tu_perfil_title),
                                            fontSize = 20.sp,
                                            fontFamily =FontFamily(Font(R.font.plusjakartabold)),
                                            color = ColorSecondary
                                        )
                                    },
                                    actions = {
                                        Box(modifier= Modifier.padding(end = 20.dp)){
                                            Text(
                                                text = stringResource(R.string.title),
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
                                    text = stringResource(R.string.subtitle),
                                    fontSize = 32.sp,
                                    fontFamily = FontFamily(Font(R.font.plusjakartabold)),
                                    color = ColorNeutralLight,
                                    modifier = Modifier.padding(top = 40.dp)
                                )
                                Text(
                                    text = stringResource(R.string.description_title),
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
                                            .clip(CircleShape)
                                            .background(
                                                ColorFields,
                                                shape = RoundedCornerShape(75.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.profilepicture), // Cambia esto por el nombre de tu archivo
                                            contentDescription = "Foto de perfil",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop

                                        )
                                    }
                                    //Icono de camara
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .align(Alignment.BottomEnd) // Lo posiciona abajo a la derecha
                                            .background(
                                                ColorTertiary,
                                                shape = RoundedCornerShape(24.dp)
                                            )
                                            .clickable { /* Aquí abrirías la galería */ },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.camera_icon),
                                            contentDescription = stringResource(R.string.change_photo),
                                            modifier = Modifier.size(18.dp),
                                            tint = ColorNeutralLight
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(32.dp))
                                //Nombre
                                Text(
                                    text = stringResource(R.string.name),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                OutlinedTextField(
                                    value = nombre,
                                    isError = nombreError,
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                        }),
                                    onValueChange = { nuevoValor ->
                                        if(nuevoValor.length<20) {
                                            nombre = nuevoValor
                                            nombreError=nombre.isEmpty()
                                        }

                                    },
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.placeholder_name),
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                    },

                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = ColorFields,
                                        focusedContainerColor = ColorFields,
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,

                                        focusedTextColor = ColorTextFields,//Color de cuando se cambia el texto
                                        unfocusedTextColor= ColorTextFields,
                                        cursorColor = ColorTextFields,
                                        //Colores cuando hay un error
                                        errorBorderColor = ColorError,
                                        errorLabelColor = ColorError,
                                        errorCursorColor = ColorTextFields,
                                        errorContainerColor = ColorFields
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    //isError=nombreError,//Es para marcar la casiila de error
                                    supportingText = {
                                        if(nombreError){
                                            Text(
                                                text= stringResource(R.string.error_message_name),
                                                color= ColorError
                                            )
                                        }

                                    }
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Apellido
                                Text(
                                    text = stringResource(R.string.surname),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                OutlinedTextField(
                                    value = apellido,
                                    isError = apellidoError,
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                        }),
                                    onValueChange = { nuevoValor ->
                                        if(apellido.length<20){
                                            apellido = nuevoValor
                                            apellidoError=apellido.isEmpty()
                                        }
                                    },
                                    supportingText = {
                                        if(apellidoError){
                                            Text(
                                                text= stringResource(R.string.error_message_surname),
                                                color=Color.Red
                                            )
                                        }

                                    },
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.placeholder_surname),
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = ColorFields,
                                        focusedContainerColor = ColorFields,
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,

                                        focusedTextColor = ColorTextFields,//Color de cuando se cambia el texto
                                        unfocusedTextColor= ColorTextFields,
                                        cursorColor = ColorTextFields,
                                        //Colores cuando hay un error
                                        errorBorderColor = ColorError,
                                        errorLabelColor = ColorError,
                                        errorCursorColor = ColorTextFields,
                                        errorContainerColor = ColorFields
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Fecha de nacimiento
                                //QUITAR EL TECLADO CUANDO ENTRE A ESTA OPCION
                                Text(
                                    text = stringResource(R.string.birthdate),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                OutlinedTextField(
                                    value = fechaNac,
                                    onValueChange = { },
                                    readOnly = true,
                                    enabled = false,
                                    isError = fechaNacError=="1" || fechaNacError=="2",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { datePickerDialog.show() },
                                    supportingText = {
                                        if (fechaNacError=="1") {
                                            Text(
                                                text = stringResource(R.string.error_message_birthdate_1),
                                                color = Color.Red
                                            )
                                        }else if(fechaNacError=="2"){
                                            Text(
                                                text = stringResource(R.string.error_message_birthdate_2),
                                                color = ColorError
                                            )
                                        }
                                    },
                                    colors = TextFieldDefaults.colors(
                                        disabledContainerColor = ColorFields,
                                        disabledTextColor = ColorTextFields,
                                        disabledIndicatorColor = if (fechaNacError=="1" || fechaNacError=="2") Color.Red else Color.Transparent,
                                        disabledLabelColor = ColorNeutral
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    placeholder = {
                                        Text(
                                            text=stringResource(R.string.placeholder_birthdate),
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Genero
                                Text(
                                    text = stringResource(R.string.gender),
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
                                            onClick = { genero = texto
                                                      if(genero.isNotEmpty()) generoError=false}, // Al hacer clic
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
                                if(generoError){
                                    Text(
                                        text= stringResource(R.string.error_message_gender),
                                        color=Color.Red,
                                        fontSize = 12.sp, // Tamaño estándar de Material 3 para errores
                                        fontFamily = FontFamily(Font(R.font.manrope)),
                                        modifier = Modifier
                                            .padding(start = 16.dp, top = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.size(32.dp))
                                //Numero de telefono, todavia me falta hacer que maneje el numero bien
                                Text(
                                    text = stringResource(R.string.phonenumber),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    fontWeight = FontWeight.Bold,
                                    color = ColorNeutral
                                )
                                KomposeCountryCodePicker(
                                    state = countryPickerState, // Asegúrate de tener: val countryPickerState = rememberKomposeCountryCodePickerState()
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = if (phoneError != "0") 1.dp else 0.dp,
                                            color = if (phoneError != "0") ColorError else Color.Transparent, // Color del borde
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    text = phoneNumber,
                                    onValueChange = {nuevoValor->
                                        if (nuevoValor.all { it.isDigit() } && nuevoValor.length <= 10){
                                        phoneNumber = nuevoValor
                                            phoneError = when {
                                                nuevoValor.isEmpty() -> "2"      // Campo vacío
                                                nuevoValor.length < 10 -> "1"   // Número incompleto
                                                else -> "0"                     // Todo bien
                                            }
                                        }
                                    },
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.placeholder_phonenumber), // O tu texto directo
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = ColorFields,
                                        unfocusedContainerColor = ColorFields,
                                        focusedTextColor = ColorTextFields,
                                        unfocusedTextColor = ColorTextFields,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        cursorColor = ColorPrimary
                                    ),
                                )

                                if(phoneError=="1"){
                                    Text(
                                        text= stringResource(R.string.error_message_phonenumber_1),
                                        color=Color.Red,
                                        fontSize = 12.sp, // Tamaño estándar de Material 3 para errores
                                        fontFamily = FontFamily(Font(R.font.manrope)),
                                        modifier = Modifier
                                            .padding(start = 16.dp, top = 4.dp)
                                    )
                                }else if(phoneError=="2"){
                                    Text(
                                        text= stringResource(R.string.error_message_phonenumber_2),
                                        color=Color.Red,
                                        fontSize = 12.sp, // Tamaño estándar de Material 3 para errores
                                        fontFamily = FontFamily(Font(R.font.manrope)),
                                        modifier = Modifier
                                            .padding(start = 16.dp, top = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.size(32.dp))
                                //Correo electronico
                                Text(
                                    text = stringResource(R.string.email),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    color = ColorNeutral
                                )
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { newValue->
                                        if(newValue.length<40) email = newValue
                                        emailError = when {
                                            newValue.isEmpty() -> "2"
                                            !emailPattern.matcher(newValue).matches() -> "1"
                                            else -> "0"
                                        }
                                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    isError = emailError=="1" || emailError=="2",
                                    placeholder = {
                                        Text(
                                            text= stringResource(R.string.placeholder_email),
                                            color = ColorNeutral,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.manrope))

                                        )
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Email    // Muestra el '@' y el '.com' en el teclado
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = ColorFields,
                                        focusedContainerColor = ColorFields,
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,

                                        focusedTextColor = ColorTextFields,//Color de cuando se cambia el texto
                                        unfocusedTextColor= ColorTextFields,
                                        cursorColor = ColorTextFields,
                                        //Colores cuando hay un error
                                        errorBorderColor = ColorError,
                                        errorLabelColor = ColorError,
                                        errorCursorColor = ColorTextFields,
                                        errorContainerColor = ColorFields
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    singleLine = true, // Importante para que no crezca hacia abajo
                                    supportingText = {
                                        if (emailError=="1") {
                                            Text(
                                                text = stringResource(R.string.error_message_email_invalid),
                                                color = Color.Red,
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily(Font(R.font.manrope))
                                            )
                                        } else if (emailError=="2") {
                                            Text(
                                                text = stringResource(R.string.error_message_email_empty),
                                                color = Color.Red,
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily(Font(R.font.manrope))
                                            )
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.size(32.dp))
                                //Intereses personales 1
                                Text(
                                    text = stringResource(R.string.personal_interest),
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
                                    text = stringResource(R.string.personal_description),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope)),
                                    color = ColorNeutral
                                )
                                TextField(
                                    value = descripcion,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    onValueChange = { nuevoValor ->
                                        if(nuevoValor.length<= 250)
                                        descripcion = nuevoValor
                                    },
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.placeholder_personal_description),
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
                                        unfocusedTextColor = ColorTextFields,
                                        cursorColor = ColorTextFields
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                                //Boton de registro
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Button(
                                            onClick = {
                                                val fullPhoneNumber = countryPickerState.getFullPhoneNumber()
                                                navController.navigate(
                                                    DataScreenDestination(
                                                        name = nombre,
                                                        apellido = apellido,
                                                        fechaNac = fechaNac,
                                                        genero= genero,
                                                        numTel= fullPhoneNumber,
                                                        email=email,
                                                        intereses= interesesSeleccionados.toList(),
                                                        descripcion= descripcion
                                                    )
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = ColorPrimary,
                                                contentColor = MyBackgroundColor,
                                                // Opcional: define colores específicos para cuando esté deshabilitado
                                                disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                                            ),
                                            enabled = esFormularioValido
                                        ) {
                                            Text(
                                                text = stringResource(R.string.end_profile),
                                                fontSize = 32.sp,
                                                fontFamily = FontFamily(Font(R.font.plusjakartabold))
                                            )
                                        }
                                        if (!esFormularioValido) {
                                            Box(
                                                modifier = Modifier
                                                    .matchParentSize() // Se pone encima del botón
                                                    .clickable {
                                                        // Acción cuando el usuario pica y está bloqueado:
                                                        // Forzamos a que se muestren los errores si los campos están vacíos
                                                        if (nombre.isEmpty()) nombreError = true
                                                        if (apellido.isEmpty()) apellidoError = true
                                                        if (fechaNac.isEmpty()) fechaNacError = "2"
                                                        if (genero.isEmpty()) generoError = true
                                                        if (phoneNumber.isEmpty()) phoneError = "2"
                                                        if (email.isEmpty()) emailError = "2"

                                                        // Aquí podrías mostrar un Toast o un aviso
                                                    }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.size(32.dp))
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
                                            text = stringResource(R.string.profles_title),
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
                                                contentDescription = stringResource(R.string.icon_arrowback_description),
                                                tint = ColorSecondary
                                            )
                                        }
                                    },
                                    actions = {
                                        Box(modifier= Modifier.padding(end = 20.dp)){
                                            Text(
                                                text = stringResource(R.string.title),
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
                                Column(modifier=Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = ColorTextFields, // Color del borde
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp)
                                ){
                                    Text(
                                        text = "${args.name} ${args.apellido}",
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily(Font(R.font.plusjakarta)),
                                        fontWeight = FontWeight.Bold,
                                        color = ColorPrimary,
                                        modifier = Modifier.padding(top = 8.dp)
                                                            .padding(bottom=16.dp)
                                    )
                                    Row{
                                        Text(
                                            text= stringResource(R.string.birthdate_show),
                                            fontFamily = FontFamily(Font(R.font.manrope)),
                                            color= ColorTertiary
                                        )
                                        Text(
                                            text=args.fechaNac,
                                            fontFamily = FontFamily(Font(R.font.manrope)),
                                            color= ColorNeutralLight
                                        )
                                    }
                                    Row{
                                        Text(
                                            text= stringResource(R.string.gender)+stringResource(R.string.dots),
                                            fontFamily = FontFamily(Font(R.font.manrope)),
                                            color= ColorTertiary
                                        )
                                        Text(
                                            text=args.genero,
                                            fontFamily = FontFamily(Font(R.font.manrope)),
                                            color= ColorNeutralLight
                                        )
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        thickness = 1.dp,
                                        color = ColorNeutral
                                    )
                                    Text(
                                        text= stringResource(R.string.contact_info_show),
                                        fontFamily = FontFamily(Font(R.font.manrope)),
                                        color= ColorTertiary
                                    )
                                    Text(
                                        text=args.numTel,
                                        fontFamily = FontFamily(Font(R.font.manrope)),
                                        color= ColorNeutralLight
                                    )
                                    Text(
                                        text=args.email,
                                        fontFamily = FontFamily(Font(R.font.manrope)),
                                        color= ColorNeutralLight
                                    )
                                    if (args.intereses.isNotEmpty()) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            thickness = 1.dp,
                                            color = ColorNeutral
                                        )
                                        Text(text = stringResource(R.string.intereses_show),
                                            fontFamily = FontFamily(Font(R.font.manrope)),
                                            color = ColorTertiary
                                        )
                                        Text(
                                            // .joinToString convierte [A, B] en "A, B"
                                            text = args.intereses.joinToString(", "),
                                            color = ColorNeutralLight
                                        )
                                    }
                                    if (args.descripcion.isNotBlank()) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            thickness = 1.dp,
                                            color = ColorNeutral
                                        )
                                        Text(text = stringResource(R.string.about_me),
                                            fontFamily = FontFamily(Font(R.font.manrope)),
                                            color = ColorTertiary
                                        )

                                        Text(
                                            text = args.descripcion,
                                            color = ColorNeutralLight,
                                            fontFamily = FontFamily(Font(R.font.manrope))
                                        )

                                    }
                                }
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