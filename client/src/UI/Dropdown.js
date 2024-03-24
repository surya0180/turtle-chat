import { KeyboardArrowDown } from "@mui/icons-material";
import {
  FormControl,
  InputBase,
  InputLabel,
  MenuItem,
  Select,
  Typography,
  styled,
  useTheme,
} from "@mui/material";
import React from "react";

const BootstrapInput = styled(InputBase)(({ theme }) => ({
  "label + &": {
    border: `1px solid ${theme.palette.primary.main}`,
    padding: 0,
  },
  "& .MuiInputBase-input": {
    padding: "2px 0px 2px 10px",
  },
}));

function Dropdown({ label, menuItems, value, onChange, ...otherProps }) {
  const { palette } = useTheme();

  return (
    <FormControl>
      <InputLabel id="demo-simple-select-label"> </InputLabel>
      <Select
        IconComponent={() => <KeyboardArrowDown color="primary" />}
        labelId="demo-simple-select-label"
        id="demo-simple-select"
        value={value}
        label="Age"
        onChange={(e) => {
          onChange(e.target.value);
        }}
        MenuProps={{
          PaperProps: {
            style: {
              backgroundColor: "black",
              border: `1px solid ${palette.primary.main}`,
            },
          },
        }}
        input={<BootstrapInput />}
        {...otherProps}
        SelectDisplayProps={{ style: { paddingRight: 0 } }}
      >
        {menuItems.map((m) => (
          <MenuItem value={m.value} key={m.value}>
            <Typography variant="caption" color="primary">
              {m.label}
            </Typography>
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}

export default Dropdown;
