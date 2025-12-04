import React, { useState, useMemo, useEffect, useCallback } from 'react';
import { MOCK_USERS, COUNTRIES, FilterState, User } from './types';
import UserCard from './components/UserCard';
import FilterControls from './components/FilterControls';
import { fetchUsersWithPets } from './services/userService';
import './App.css';

const App: React.FC = () => {
  const [filterState, setFilterState] = useState<FilterState>({
    selectedCountry: '',
    userCount: 50
  });

  const [users, setUsers] = useState<User[]>(MOCK_USERS);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  // Track the last fetched parameters to avoid unnecessary API calls
  const [lastFetchedParams, setLastFetchedParams] = useState<FilterState | null>(null);

  // Display users based on current filter state
  // Note: Country filtering is handled by the backend
  const filteredUsers = useMemo(() => {
    return users.slice(0, filterState.userCount);
  }, [users, filterState.userCount]);

  // Check if current filter state is different from last fetched params
  const hasParamsChanged = useMemo(() => {
    if (!lastFetchedParams) return true; // Always allow first fetch
    return (
      lastFetchedParams.selectedCountry !== filterState.selectedCountry ||
      lastFetchedParams.userCount !== filterState.userCount
    );
  }, [filterState, lastFetchedParams]);

  // Function to fetch data from backend API
  // Wrapped in useCallback to prevent unnecessary re-creation
  const handleFetchData = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const fetchedUsers = await fetchUsersWithPets({
        nat: filterState.selectedCountry || undefined,
        results: filterState.userCount
      });
      setUsers(fetchedUsers);
      // Update last fetched params after successful fetch
      setLastFetchedParams({ ...filterState });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch users');
      console.error('Error fetching users:', err);
    } finally {
      setLoading(false);
    }
  }, [filterState]); // Recreate only when filterState changes

  // Auto-fetch data on component mount
  useEffect(() => {
    handleFetchData();
  }, [handleFetchData]); // Safe to include now that it's memoized

  return (
    <div className="font-display bg-background-light dark:bg-background-dark text-text-primary-light dark:text-text-primary-dark">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {/* Header */}
        <header className="flex items-start space-x-4 mb-8">
          <div className="flex-shrink-0">
            <div className="w-12 h-12 rounded-lg bg-primary/10 dark:bg-primary/20 flex items-center justify-center">
              <span className="material-symbols-outlined text-primary text-3xl">pets</span>
            </div>
          </div>
          <div>
            <h1 className="text-2xl font-bold text-text-primary-light dark:text-text-primary-dark">
              Users & Their Pets
            </h1>
            <p className="text-text-secondary-light dark:text-text-secondary-dark mt-1">
              Browse users from different countries and see their beloved pets
            </p>
          </div>
        </header>

        {/* Filter Controls */}
        <FilterControls
          filterState={filterState}
          onFilterChange={setFilterState}
          totalUsers={users.length}
          countries={COUNTRIES}
          onFetchData={handleFetchData}
          loading={loading}
          hasParamsChanged={hasParamsChanged}
        />

        {/* Error State */}
        {error && (
          <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg p-4 mb-8">
            <div className="flex items-center space-x-2">
              <span className="material-symbols-outlined text-red-600 dark:text-red-400">error</span>
              <p className="text-red-800 dark:text-red-300 font-medium">
                {error}
              </p>
            </div>
          </div>
        )}

        {/* Loading State */}
        {loading ? (
          <div className="text-center py-12">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
            <p className="text-text-secondary-light dark:text-text-secondary-dark text-lg mt-4">
              Loading users...
            </p>
          </div>
        ) : (
          <>
            {/* User Grid */}
            <main className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
              {filteredUsers.map((user) => (
                <UserCard key={user.id} user={user} />
              ))}
            </main>

            {/* Empty State */}
            {filteredUsers.length === 0 && (
              <div className="text-center py-12">
                <p className="text-text-secondary-light dark:text-text-secondary-dark text-lg">
                  No users found for the selected criteria.
                </p>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default App;
