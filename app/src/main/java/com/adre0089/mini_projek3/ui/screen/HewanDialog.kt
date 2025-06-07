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

@Composable
fun HewanDialog(
    bitmap: Bitmap?,
    jaket: Jaket? = null,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String, String, String?) -> Unit,
    onChangeImageRequest: () -> Unit = {} // Tambahkan event untuk ganti gambar
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
                    modifier = Modifier.padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = jenis,
                    onValueChange = { jenis = it },
                    label = { Text(stringResource(R.string.nama_latin)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text(stringResource(R.string.status)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
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
