console.log(
  process.env,
  process.env.REACT_APP_API_HOST,
  process.env.REACT_APP_API_HOST,
  process.env.REACT_APP_SOCKET_PORT
);
export const API_HOST = process.env.REACT_APP_API_HOST || "lochost";
export const API_PORT = process.env.REACT_APP_API_PORT || "800";
export const SOCKET_PORT = process.env.REACT_APP_SOCKET_PORT || "0";
