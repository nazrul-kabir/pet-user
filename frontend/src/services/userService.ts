import { User, Country } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

export interface FetchUsersParams {
  nat?: string;
  results?: number;
}

// Backend API response interface
interface BackendUserResponse {
  id: string;
  gender: string;
  country: string;
  name: string;
  email: string;
  dob: {
    date: string;
    age: number;
  };
  phone: string;
  petImage: string;
}

// Country code to name mapping
const COUNTRY_NAMES: Record<string, string> = {
  AU: 'Australia',
  BR: 'Brazil',
  CA: 'Canada',
  CH: 'Switzerland',
  DE: 'Germany',
  DK: 'Denmark',
  ES: 'Spain',
  FI: 'Finland',
  FR: 'France',
  GB: 'United Kingdom',
  IE: 'Ireland',
  IN: 'India',
  IR: 'Iran',
  MX: 'Mexico',
  NL: 'Netherlands',
  NO: 'Norway',
  NZ: 'New Zealand',
  RS: 'Serbia',
  SE: 'Sweden',
  TR: 'Turkey',
  UA: 'Ukraine',
  US: 'United States',
};

// Transform backend response to frontend User format
const transformUser = (backendUser: BackendUserResponse, index: number): User => {
  const countryCode = backendUser.country.toUpperCase();
  const country: Country = {
    code: countryCode,
    name: COUNTRY_NAMES[countryCode] || countryCode,
  };

  // Convert date to DD/MM/YYYY format
  const date = new Date(backendUser.dob.date);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();
  const birthDate = `${day}/${month}/${year}`;

  return {
    id: index + 1,
    name: backendUser.name,
    age: backendUser.dob.age,
    country,
    countryCode,
    email: backendUser.email,
    phone: backendUser.phone,
    birthDate,
    petImageUrl: backendUser.petImage,
    petAltText: `A dog belonging to ${backendUser.name}`,
  };
};

export const fetchUsersWithPets = async (params?: FetchUsersParams): Promise<User[]> => {
  try {
    const url = new URL(`${API_BASE_URL}/users-with-pet`);

    // Add query parameters if provided
    if (params?.nat) {
      url.searchParams.append('nat', params.nat);
    }

    if (params?.results) {
      url.searchParams.append('results', params.results.toString());
    }

    const response = await fetch(url.toString());

    if (!response.ok) {
      throw new Error(`Failed to fetch users: ${response.statusText}`);
    }

    const data: BackendUserResponse[] = await response.json();

    // Transform backend response to frontend format
    return data.map((user, index) => transformUser(user, index));
  } catch (error) {
    console.error('Error fetching users with pets:', error);
    throw error;
  }
};
