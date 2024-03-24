import { Box, TextField, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";

function Input({ label, value, onEnter, color, disableCursor, variant }) {
  if (!color) color = "primary";
  if (!variant) variant = "caption";

  const [text, setText] = useState(value || "");
  const [cursor, setCursor] = useState(true);
  const [input, setInput] = useState(true);
  const [isTyping, setIsTyping] = useState(false);

  useEffect(() => {
    if (isTyping) {
      const timeout = setTimeout(() => {
        setIsTyping(false);
      }, [500]);
      return () => {
        clearTimeout(timeout);
      };
    }
  }, [isTyping]);

  function startBlinking() {
    return setInterval(() => {
      setCursor((prevShowCursor) => !prevShowCursor);
    }, 500);
  }

  useEffect(() => {
    if (input || disableCursor) {
      const interval = startBlinking();
      return () => {
        clearInterval(interval);
      };
    }
  }, [input, disableCursor]);

  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        gap: "0.5rem",
        position: "relative",
        width: "100%",
      }}
    >
      <Typography variant={variant} color={color}>
        {label || "No label provided"} :
      </Typography>
      <Typography
        variant={variant}
        color={color}
        sx={{ flex: 1, display: "flex" }}
      >
        {text}
        {(isTyping || cursor) && (
          <Typography variant={variant} fontWeight={800} color={color}>
            _
          </Typography>
        )}
      </Typography>
      {input && (
        <TextField
          inputRef={(input) => input && input.focus()}
          value={text}
          autoComplete="off"
          autoFocus={true}
          onChange={(e) => {
            if (e.target?.value?.length <= 70) {
              setIsTyping(true);
              setText(e.target.value);
            }
          }}
          onKeyDown={(e) => {
            if (e.nativeEvent.key === "Enter") {
              setInput(false);
              onEnter(text);
              setCursor(false);
            }
          }}
          sx={{
            width: "1rem",
            position: "absolute",
            left: "0.5rem",
            height: "1rem",
            opacity: 0,
          }}
          InputProps={{ style: { height: "1rem" } }}
          size="small"
        />
      )}
    </Box>
  );
}

export default Input;
