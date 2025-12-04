// TypeScript interfaces for the user and pet application

export interface User {
  id: number;
  name: string;
  age: number;
  country: Country;
  countryCode: string;
  email: string;
  phone: string;
  birthDate: string; // Format: DD/MM/YYYY
  petImageUrl: string;
  petAltText: string;
}

export interface Country {
  code: string; // e.g., 'SE', 'FI', 'GB', 'NO'
  name: string; // e.g., 'Sweden', 'Finland', 'United Kingdom', 'Norway'
}

export const COUNTRIES: Country[] = [
  { code: "AU", name: "Australia" },
  { code: "BR", name: "Brazil" },
  { code: "CA", name: "Canada" },
  { code: "CH", name: "Switzerland" },
  { code: "DE", name: "Germany" },
  { code: "DK", name: "Denmark" },
  { code: "ES", name: "Spain" },
  { code: "FI", name: "Finland" },
  { code: "FR", name: "France" },
  { code: "GB", name: "United Kingdom" },
  { code: "IE", name: "Ireland" },
  { code: "IN", name: "India" },
  { code: "IR", name: "Iran" },
  { code: "MX", name: "Mexico" },
  { code: "NL", name: "Netherlands" },
  { code: "NO", name: "Norway" },
  { code: "NZ", name: "New Zealand" },
  { code: "RS", name: "Serbia" },
  { code: "SE", name: "Sweden" },
  { code: "TR", name: "Turkey" },
  { code: "UA", name: "Ukraine" },
  { code: "US", name: "United States" },
];

export interface FilterState {
  selectedCountry: string;
  userCount: number;
}