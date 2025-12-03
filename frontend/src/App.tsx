import React, { useState, useMemo } from 'react';
import { MOCK_USERS, COUNTRIES, FilterState } from './types';
import UserCard from './components/UserCard';
import FilterControls from './components/FilterControls';
import './App.css';

const App: React.FC = () => {
  const [filterState, setFilterState] = useState<FilterState>({
    selectedCountry: '',
    userCount: 100
  });

  // Filter and slice users based on current filter state
  const filteredUsers = useMemo(() => {
    let users = MOCK_USERS;
    
    // Filter by country if selected
    if (filterState.selectedCountry) {
      users = users.filter(user => user.countryCode === filterState.selectedCountry);
    }
    
    // Limit number of users
    return users.slice(0, filterState.userCount);
  }, [filterState]);

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
          totalUsers={MOCK_USERS.length}
          countries={COUNTRIES}
        />

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
      </div>
    </div>
  );
};

export default App;
