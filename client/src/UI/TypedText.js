import { Typography } from "@mui/material";
import React, { useEffect, useState } from "react";

function TypedText({ text, variant, color, delay }) {
  if (!text) text = "N/A";
  if (!variant) variant = "body1";
  if (!color) color = "primary";
  if (!delay) delay = 100;

  const [typed, setTyped] = useState("");
  const [index, setIndex] = useState(0);
  const [cursor, setCursor] = useState(true);

  const startBlinking = () => {
    setInterval(() => {
      setCursor((prevShowCursor) => !prevShowCursor);
    }, 700);
  };

  useEffect(() => {
    if (index < text.length) {
      const timeout = setTimeout(() => {
        setTyped((prevState) => prevState + text[index]);
        setIndex((prevState) => prevState + 1);
      }, [delay]);
      return () => clearTimeout(timeout);
    } else {
      startBlinking();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [index]);

  return (
    <Typography variant={variant} color={color}>
      {typed}
      {cursor && <span>_</span>}
    </Typography>
  );
}

export default TypedText;
