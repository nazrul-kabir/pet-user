import React from 'react';
import { Country, FilterState } from '../types';

interface FilterControlsProps {
  filterState: FilterState;
  onFilterChange: (newFilter: FilterState) => void;
  totalUsers: number;
  countries: Country[];
  onFetchData: () => void;
  loading: boolean;
}

const FilterControls: React.FC<FilterControlsProps> = ({
  filterState,
  onFilterChange,
  totalUsers,
  countries,
  onFetchData,
  loading
}) => {
  const handleCountryChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onFilterChange({
      ...filterState,
      selectedCountry: e.target.value
    });
  };

  const handleUserCountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const count = parseInt(e.target.value) || 0;
    onFilterChange({
      ...filterState,
      userCount: count
    });
  };

  const handleFetchData = () => {
    onFetchData();
  };

  const displayedCount = Math.min(filterState.userCount, totalUsers);

  return (
    <div className="bg-card-light dark:bg-card-dark border border-border-light dark:border-border-dark p-6 rounded-lg mb-8 shadow-sm hover:shadow-lg transition-shadow duration-300">
      <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-5 gap-6 items-end">
        {/* Country Filter */}
        <div className="lg:col-span-2">
          <label 
            className="block text-sm font-medium text-text-secondary-light dark:text-text-secondary-dark mb-1" 
            htmlFor="country"
          >
            Filter by Country
          </label>
          <select
            className="w-full rounded-md border-border-light dark:border-border-dark bg-background-light dark:bg-background-dark focus:ring-2 focus:ring-primary focus:border-primary text-text-primary-light dark:text-text-primary-dark transition-all duration-200 hover:border-gray-300 dark:hover:border-gray-500 appearance-none pr-8 focus:outline-none shadow-sm"
            id="country"
            value={filterState.selectedCountry}
            onChange={handleCountryChange}
          >
            <option value="">All Countries</option>
            {countries.map((country) => (
              <option key={country.code} value={country.code}>
                {country.name}
              </option>
            ))}
          </select>
        </div>

        {/* User Count Input */}
        <div>
          <label 
            className="block text-sm font-medium text-text-secondary-light dark:text-text-secondary-dark mb-1" 
            htmlFor="users"
          >
            Number of Users to Fetch
          </label>
          <input 
            className="w-full rounded-md border-border-light dark:border-border-dark bg-background-light dark:bg-background-dark focus:ring-2 focus:ring-primary focus:border-primary text-text-primary-light dark:text-text-primary-dark transition-all duration-200 hover:border-gray-300 dark:hover:border-gray-500" 
            id="users" 
            type="number" 
            value={filterState.userCount}
            onChange={handleUserCountChange}
            min="1"
            max={totalUsers}
          />
        </div>

        {/* Action Button */}
        <div className="lg:col-span-2 flex items-center justify-between md:justify-start space-x-4">
          <button
            className="w-full md:w-auto bg-[#161B22] dark:bg-white text-white dark:text-black font-semibold py-2 px-4 rounded-md flex items-center justify-center space-x-2 hover:opacity-90 transition-all duration-200 transform hover:scale-105 shadow-md hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
            onClick={handleFetchData}
            disabled={loading}
          >
            {loading ? (
              <>
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white dark:border-black"></div>
                <span>Loading...</span>
              </>
            ) : (
              <>
                <span className="material-symbols-outlined text-base">download</span>
                <span>Fetch Data</span>
              </>
            )}
          </button>
          <p className="text-sm text-text-secondary-light dark:text-text-secondary-dark shrink-0">
            Showing <span className="font-semibold text-text-primary-light dark:text-text-primary-dark">{displayedCount}</span> users
          </p>
        </div>
      </div>
    </div>
  );
};

export default FilterControls;