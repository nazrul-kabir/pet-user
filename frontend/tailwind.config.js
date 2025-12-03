/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        primary: "#0d7ff2",
        "background-light": "#f5f7f8",
        "background-dark": "#101922",
        "card-light": "#FFFFFF",
        "card-dark": "#161B22",
        "border-light": "#E2E8F0",
        "border-dark": "#30363D",
        "text-primary-light": "#1E293B",
        "text-primary-dark": "#E6EDF3",
        "text-secondary-light": "#64748B",
        "text-secondary-dark": "#8B949E",
      },
      fontFamily: {
        display: "Inter",
      },
      borderRadius: {
        DEFAULT: "0.25rem",
        lg: "0.5rem",
        xl: "0.75rem",
        full: "9999px",
      },
    },
  },
  plugins: [],
}