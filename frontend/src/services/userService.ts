import { User, Country, COUNTRIES } from '../types';

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

// Transform backend response to frontend User format
const transformUser = (backendUser: BackendUserResponse, index: number): User => {
  const countryCode = backendUser.country.toUpperCase();

  // Find country from centralized COUNTRIES array
  const countryData = COUNTRIES.find(c => c.code === countryCode);
  const country: Country = countryData || {
    code: countryCode,
    name: countryCode,
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
