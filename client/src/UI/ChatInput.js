import { SendOutlined } from "@mui/icons-material";
import {
  Box,
  IconButton,
  InputAdornment,
  TextField,
  useTheme,
} from "@mui/material";
import React, { useState } from "react";

function ChatInput({ onSend, onEnter }) {
  const { palette } = useTheme();
  const [value, setValue] = useState("");
  return (
    <Box
      sx={{
        height: "5rem",
        width: "100%",
        padding: "0 1rem",
      }}
    >
      <TextField
        autoComplete="off"
        InputProps={{
          endAdornment: (
            <InputAdornment
              sx={{
                padding: "1rem",
              }}
            >
              <IconButton
                onClick={() => {
                  onSend(value);
                  setValue("");
                }}
              >
                <SendOutlined color="primary" />
              </IconButton>
            </InputAdornment>
          ),
          sx: { paddingRight: 0 },
        }}
        inputProps={{
          style: {
            width: "100%",
            color: palette.primary.main,
            border: `2px solid ${palette.primary.main}`,
          },
        }}
        value={value}
        onKeyDown={(e) => {
          console.log(e.nativeEvent.key);
          if (e.nativeEvent.key === "Enter") {
            onEnter(value);
            setValue("");
          }
        }}
        onChange={(e) => {
          setValue(e.target.value);
        }}
        sx={{
          "& .MuiOutlinedInput-root": {
            "& fieldset": {
              border: `2px solid ${palette.primary.main}`,
              paddingRight: 0,
            },
            "&:hover fieldset": {
              border: `2px solid ${palette.primary.main}`,
            },
            "&.Mui-focused fieldset": {
              border: `2px solid ${palette.primary.main}`,
            },
          },
          width: "100%",
        }}
      />
    </Box>
  );
}

export default ChatInput;
