package com.movie_app_task.feature.movie_list.ui.screen.home.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movie_app_task.R

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    onSearchClick: (String) -> Unit = {},
    endIcon: Painter,
    onEndIconClick: () -> Unit = {},
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = "Search...") },
            shape = RoundedCornerShape(20.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                disabledIndicatorColor = Transparent,
                errorIndicatorColor = Transparent,
                focusedContainerColor = Color(0xFFF0F0F0),
                unfocusedContainerColor = Color(0xFFF0F0F0)
            ), leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon",
                    tint = Color.Gray,
                    modifier = Modifier.clickable { onSearchClick(value) }
                )
            },
            trailingIcon = {
                Icon(
                    painter = endIcon,
                    contentDescription = "End Icon",
                    modifier = Modifier.clickable(onClick = onEndIconClick),
                    tint = Color.Gray
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    val dummyIcon = painterResource(R.drawable.ic_voice)
    HomeHeader(
        endIcon = dummyIcon,
        onEndIconClick = {},
        onSearchClick = {},
        onValueChange = {},
        value = ""
    )
}