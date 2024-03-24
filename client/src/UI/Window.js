import { Close, HorizontalRule, WebAsset } from "@mui/icons-material";
import { Box, IconButton, Modal, Paper, Stack, useTheme } from "@mui/material";
import React, { useEffect } from "react";

function Window({ open, onClose, children }) {
  const { palette } = useTheme();
  useEffect(() => {
    if (!open) {
      return () => {};
    }
  }, [open]);
  return (
    <Modal
      open={open}
      disableRestoreFocus
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Paper
        variant="outlined"
        sx={{
          backgroundColor: `${palette.background.default}`,
          border: "1px solid white",
          margin: "auto",
          boxSizing: "border-box",
        }}
      >
        <Stack>
          <Box
            sx={{
              display: "flex",
              gap: "0.5rem",
              alignItems: "center",
              borderBottom: "1px solid white",
              padding: "0.5rem",
            }}
          >
            <IconButton
              size="small"
              sx={{ padding: 0 }}
              onClick={() => {
                onClose();
              }}
            >
              <Close
                sx={{
                  fontSize: "0.8rem",
                  backgroundColor: `${palette.error.main}`,
                  borderRadius: "1rem",
                }}
              />
            </IconButton>
            <IconButton size="small" sx={{ padding: 0 }}>
              <HorizontalRule
                sx={{
                  fontSize: "0.8rem",
                  backgroundColor: `${palette.success.main}`,
                  borderRadius: "1rem",
                }}
              />
            </IconButton>
            <IconButton size="small" sx={{ padding: 0 }}>
              <WebAsset
                sx={{
                  fontSize: "0.8rem",
                  backgroundColor: `${palette.primary.main}`,
                  borderRadius: "1rem",
                  padding: "0.1rem",
                }}
              />
            </IconButton>
          </Box>
          {children}
        </Stack>
      </Paper>
    </Modal>
  );
}

export default Window;
