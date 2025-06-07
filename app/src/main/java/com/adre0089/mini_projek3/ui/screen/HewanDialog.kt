package com.adre0089.mini_projek3.ui.screen

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.adre0089.mini_projek3.R
import com.adre0089.mini_projek3.model.Jaket
import com.adre0089.mini_projek3.network.JaketApi
import com.adre0089.mini_projek3.ui.theme.Mobpro3Theme

// Pastikan definisi ini ada di tempat yang bisa diakses oleh HewanDialog
val jacketTypes = listOf(
    "Bomber Jacket", "Denim Jacket", "Leather Jacket", "Parka", "Windbreaker",
    "Blouson", "Track Jacket", "Military Jacket", "Puffer Jacket", "Fleece Jacket"
)

val jacketStatusOptions = listOf("Available", "Not Available")


@OptIn(ExperimentalMaterial3Api::class) // Perlu ditambahkan untuk ExposedDropdownMenuBox
@Composable
fun HewanDialog(
    bitmap: Bitmap?,
    jaket: Jaket? = null,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String, String, String?) -> Unit,
    onChangeImageRequest: () -> Unit = {}
) {
    var nama by remember { mutableStateOf(jaket?.nama ?: "") }
    var jenis by remember { mutableStateOf(jaket?.jenis ?: "") }
    var status by remember { mutableStateOf(jaket?.status ?: "") }

    val isUpdateMode = jaket != null

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                } else if (isUpdateMode && !jaket?.gambar.isNullOrEmpty()) {
                    AsyncImage(
                        model = JaketApi.getJaketImageUrl(jaket!!.gambar),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }

                OutlinedButton(
                    onClick = { onChangeImageRequest() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Ganti Gambar")
                }

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text(stringResource(R.string.nama)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth() // Tambahkan fillMaxWidth
                )

                // Dropdown untuk Jenis Jaket
                JacketTypeDropdownInDialog( // Fungsi baru untuk dialog
                    selectedType = jenis,
                    onTypeSelected = { jenis = it },
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                )

                // Dropdown untuk Status Jaket
                JacketStatusDropdownInDialog( // Fungsi baru untuk dialog
                    selectedStatus = status,
                    onStatusSelected = { status = it },
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(stringResource(R.string.batal))
                    }
                    OutlinedButton(
                        onClick = { onConfirmation(nama, jenis, status, jaket?.id) },
                        enabled = nama.isNotEmpty() && jenis.isNotEmpty() && status.isNotEmpty(),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(
                                if (isUpdateMode) R.string.update else R.string.simpan
                            )
                        )
                    }
                }
            }
        }
    }
}

// --- FUNGSI DROPDOWN UNTUK DIGUNAKAN DI DALAM DIALOG ---
// Saya mengganti namanya menjadi `...InDialog` untuk menghindari konflik jika Anda juga punya di DetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JacketTypeDropdownInDialog(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false // Tambahkan isError jika Anda ingin validasi di dialog juga
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = selectedType,
            onValueChange = {},
            readOnly = true,
            isError = isError,
            label = { Text("Jenis Jaket") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            placeholder = { Text("Pilih jenis jaket") },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            jacketTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JacketStatusDropdownInDialog(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false // Tambahkan isError jika Anda ingin validasi di dialog juga
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = selectedStatus,
            onValueChange = {},
            readOnly = true,
            isError = isError,
            label = { Text("Status Jaket") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            placeholder = { Text("Pilih status jaket") },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            jacketStatusOptions.forEach { statusOption ->
                DropdownMenuItem(
                    text = { Text(statusOption) },
                    onClick = {
                        onStatusSelected(statusOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddDialogPreview() {
    Mobpro3Theme {
        HewanDialog(
            bitmap = null,
            onDismissRequest = {},
            onConfirmation = { _, _, _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun UpdateDialogPreview() {
    Mobpro3Theme {
        HewanDialog(
            bitmap = null,
            jaket = Jaket("id123", "Jaket Preview", "Kulit", "Tersedia", "gambar.jpg"),
            onDismissRequest = {},
            onConfirmation = { _, _, _, _ -> }
        )
    }
}