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

// Mock data
export const MOCK_USERS: User[] = [
  {
    id: 1,
    name: "دينا كامروا",
    age: 61,
    country: { code: "SE", name: "Sweden" },
    countryCode: "SE",
    email: "byt.khmrw@example.com",
    phone: "001-20752168",
    birthDate: "31/01/1964",
    petImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuAk2lHQwDZoKK5IZYxnDcAHh4Vbx4K8zfqfq9h6jX0oiI8vPWsaPrwaecXsVO6g1znCUBML4CSdIvznFpDLEAGnAeQjH99BFGyMNqDp-K0s1EycfWno2jhVewRB52uDCFXNw-DYtYbsvoEcqGF8EnyzMfgBhExmWBucnlTxy42YB7hGPD8U4NvR0RS2DxvHyNjeGEiuPDhPm2sX-CDTNheuoJjMEZiGbkJs2aMzkHp-6do7UkZ72sKUqJcXWskinMgm-qC91OIakA",
    petAltText: "A brown dog wearing a red bandana sitting in the driver's seat of a car."
  },
  {
    id: 2,
    name: "Sofie Sørensen",
    age: 78,
    country: { code: "FI", name: "Finland" },
    countryCode: "FI",
    email: "sofie.sorensen@example.com",
    phone: "27433081",
    birthDate: "27/05/1947",
    petImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuANhF6AQiUQzExN55R0y5zCE4_Lvue8I4Ub6lkWm3ArOD9DgB08XN3Ws8H8vC_ru8PhC3YG9XSo61thWwJD3JPI-Bqwk3h87bohxy6-R1N8EC1wWtsJf-tCDvVQstIVD20p9ZEV9IKTFPnI0PqmcrZf7jiqqQ5Vd-Nwwrzrg44NFINcwyazu81ovpImhLHbmjgaebhzacIAXnYrdVMCC4yDtXBJ9fHMJtEVBBP3hia88cRLzIKvd46b6wmto9WlUDvSGRqwodmpGA",
    petAltText: "A grey poodle-like dog standing against a pink wall."
  },
  {
    id: 3,
    name: "Annouk Dorrestijn",
    age: 77,
    country: { code: "SE", name: "Sweden" },
    countryCode: "SE",
    email: "annouk.dorrestijn@example.com",
    phone: "(0124) 098046",
    birthDate: "27/01/1948",
    petImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuCNeXmKqN0yMQoHoixAyjg-UM5eg2dEiVv-VR1TDJAq7fTsm5gA9gVk67Hg13evNtV4AkHWCc9ziQW8OYn6Rvckar8j2BkvPaZ9BqCD6AZAn8oOvhQtOFcDDc8EVEGon5qMFK-cLiYq2bZ0-eYR1ApmjY4qk-lwVftMn_uybtt8k06L8X7i2hUs0v_pxSFiDzJKrCiBSxQkY7tRCnnl4ZytqBPuN9v8XM_huN8Df-s-T_G9S8xaIPXxY2lVnumHB8ykl-0Bo1PVLQ",
    petAltText: "A happy Shiba Inu dog smiling in a grassy field with a yellow flower."
  },
  {
    id: 4,
    name: "Ayaan Dalvi",
    age: 71,
    country: { code: "SE", name: "Sweden" },
    countryCode: "SE",
    email: "ayaan.dalvi@example.com",
    phone: "9523703749",
    birthDate: "29/05/1954",
    petImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuBmcpRYcvARPHx6QwZcR3TKj6nq00hDFKGLY0jsRhio8O2IJP4jqki-km2bWhqmiP_N7003Qt8sXA-rFvBAJiI5kwPr5F17fc1c0qoKDJ_em9U2KxR4rTrLEKHX4HvrduQxEjpXpEUQ53ZsikOntqPoT1uEompSR_itdR4mTu24dzRxMLUQx2y8HvKeGpTCJ62O_JQSCBKA1q9wxLwWHUmxVBz4xkS_ejR0jkuNr1Mq-bls_YmBMe5xOsZYsufUtSU48bXzdbgG7g",
    petAltText: "A small Jack Russell Terrier with its mouth open, looking at the camera."
  },
  {
    id: 5,
    name: "Volker König",
    age: 37,
    country: { code: "GB", name: "United Kingdom" },
    countryCode: "GB",
    email: "volker.konig@example.com",
    phone: "0483-0908245",
    birthDate: "15/05/1988",
    petImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuAXdHlqOhGCkZcoUi6oBhPAXJ3lCGLMRp15kzEoobA0xl4xe5OjB4prZSFQv9xzsEV_RjT7UmEpxwvCzbsxAIrita6mY2-zQ97erm6p4FnvmKgWgG4eFwyLRefC-6WO7QwYjCMY45fppAULsvAkH-cpDEmogZ66iBKTZdFdLvy3f03-JqL9j60KOsVce9ZEBfUcViu9hF_K6rUG7IdcDxPM-HZQJ3-EZpJ0PtH3Ic2xFKOtmJHHfLv-TtAgX8SIAnVnX1aEK4jSWg",
    petAltText: "A fluffy black and brown dog standing in a grassy area."
  },
  {
    id: 6,
    name: "Brayden Washington",
    age: 36,
    country: { code: "NO", name: "Norway" },
    countryCode: "NO",
    email: "brayden.washington@example.com",
    phone: "0141 853 3069",
    birthDate: "07/05/1989",
    petImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuAxmNU4sTs7qVQLb65rA20fNG_52WKD5mHjzncSb__dC_NI4PZYzuYQCIiEXX6pMM1fyjdvyO5OTYn6xhplZzngTiuQiF_NDzY-2LwNq4_Lc7QGhIy-3HoitzFt4DBgBFdOjuSxtBKTiMZSQGPWJn6OMQ_Dl0fqV0K4-eqvp0_AIiK8_lRZQoORNG30cS8IvL3olqACs4zqiSIxnFFSEtqx1gqJd5iwXQqi6gDvO4oNE1KiEdE-VnHXJs8JpeQ5n_cVQEeHADAE_A",
    petAltText: "A white and light brown dog with pointy ears looking directly at the camera."
  }
];

export const COUNTRIES: Country[] = [
  { code: "SE", name: "Sweden" },
  { code: "FI", name: "Finland" },
  { code: "GB", name: "United Kingdom" },
  { code: "NO", name: "Norway" },
];

export interface FilterState {
  selectedCountry: string;
  userCount: number;
}