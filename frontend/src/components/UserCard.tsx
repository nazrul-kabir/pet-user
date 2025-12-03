import React from 'react';
import { User } from '../types';

// TODO: Consider splitting the card into smaller subcomponents for better maintainability
// TODO: Add error handling for missing/invalid user fields (e.g., fallback image if petImageUrl is missing)
// TODO: Add loading and error states
// TODO: Consider accessibility improvements (e.g., alt text, ARIA labels, keyboard navigation)
// TODO: Add animation or hover effects for better UX

interface UserCardProps {
  user: User;
}

const UserCard: React.FC<UserCardProps> = ({ user }) => {
  return (
    <div className="bg-card-light dark:bg-card-dark border border-border-light dark:border-border-dark rounded-lg overflow-hidden shadow-sm">
      <img 
        alt={user.petAltText} 
        className="w-full h-56 object-cover" 
        src={user.petImageUrl}
      />
      <div className="p-6">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h2 className="text-lg font-semibold text-text-primary-light dark:text-text-primary-dark">
              {user.name}
            </h2>
            <span className="text-sm font-medium bg-blue-100 dark:bg-blue-900/50 text-blue-600 dark:text-blue-300 py-0.5 px-2 rounded-full">
              {user.countryCode}
            </span>
          </div>
          <div className="text-right text-text-secondary-light dark:text-text-secondary-dark">
            <span className="text-lg font-semibold text-text-primary-light dark:text-text-primary-dark">
              {user.age}
            </span>
            <p className="text-xs">years</p>
          </div>
        </div>
        <div className="space-y-3 text-sm text-text-secondary-light dark:text-text-secondary-dark">
          <div className="flex items-center space-x-3">
            <span className="material-symbols-outlined text-lg">mail</span>
            <span>{user.email}</span>
          </div>
          <div className="flex items-center space-x-3">
            <span className="material-symbols-outlined text-lg">call</span>
            <span>{user.phone}</span>
          </div>
          <div className="flex items-center space-x-3">
            <span className="material-symbols-outlined text-lg">calendar_today</span>
            <span>{user.birthDate}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserCard;