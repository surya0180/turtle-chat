import { Box, IconButton } from "@mui/material";
import React from "react";
import { Turtle2 } from "../assets/icons";
import { GitHub, InsertEmoticonOutlined } from "@mui/icons-material";
import Text from "./Text";

function Header({ userData }) {
  return (
    <Box
      sx={{
        width: "100%",
        borderBottom: "1px solid white",
        paddingLeft: "0.5rem",
        paddingRight: "0",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        height: "5rem",
      }}
    >
      <IconButton>
        <Turtle2 />
      </IconButton>
      <Box sx={{ display: "flex" }}>
        {userData && (
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              gap: "0.5rem",
              borderLeft: "1px solid white",
              padding: "0 1rem",
            }}
          >
            <InsertEmoticonOutlined fontSize="small" color="primary" />
            <Text
              color={"primary"}
              text={`${userData?.userName} (#${userData?.userId})`}
            />
          </Box>
        )}
        <Box sx={{ borderLeft: "1px solid white" }}>
          <IconButton sx={{ margin: "1rem" }}>
            <GitHub color="primary" />
          </IconButton>
        </Box>
      </Box>
    </Box>
  );
}

export default Header;
