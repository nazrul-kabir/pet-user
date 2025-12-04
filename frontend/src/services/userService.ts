import { User } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

export interface FetchUsersParams {
  country?: string;
  limit?: number;
}

export const fetchUsersWithPets = async (params?: FetchUsersParams): Promise<User[]> => {
  try {
    const url = new URL(`${API_BASE_URL}/users-with-pet`);

    // Add query parameters if provided
    if (params?.country) {
      url.searchParams.append('country', params.country);
    }

    if (params?.limit) {
      url.searchParams.append('limit', params.limit.toString());
    }

    const response = await fetch(url.toString());

    if (!response.ok) {
      throw new Error(`Failed to fetch users: ${response.statusText}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching users with pets:', error);
    throw error;
  }
};
