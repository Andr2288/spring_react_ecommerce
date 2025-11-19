import { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore.js";
import { useCartStore } from "../store/useCartStore.js";
import {
    ShoppingCart,
    User,
    Menu,
    X,
    LogOut,
    Home,
    Package,
    Shield,
    FileText,
    Users,
    ChevronDown
} from "lucide-react";

const Navbar = () => {
    const { authUser, logout } = useAuthStore();
    const { totalItems } = useCartStore();
    const location = useLocation();

    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [isAdminDropdownOpen, setIsAdminDropdownOpen] = useState(false);

    const handleLogout = () => {
        logout();
        setIsMenuOpen(false);
        setIsAdminDropdownOpen(false);
    };

    const closeMenus = () => {
        setIsMenuOpen(false);
        setIsAdminDropdownOpen(false);
    };

    if (!authUser) return null;

    return (
        <nav className="bg-white shadow-lg border-b border-gray-200">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    {/* Logo */}
                    <div className="flex items-center">
                        <Link to="/home" onClick={closeMenus} className="flex items-center space-x-2">
                            <div className="bg-blue-600 p-2 rounded-lg">
                                <ShoppingCart className="h-6 w-6 text-white" />
                            </div>
                            <span className="text-xl font-bold text-gray-900">E-Commerce</span>
                        </Link>
                    </div>

                    {/* Desktop Navigation */}
                    <div className="hidden md:flex md:items-center md:space-x-4">
                        {/* Regular Navigation */}
                        <div className="flex items-center space-x-1">
                            <Link
                                to="/home"
                                className={`flex items-center px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                    location.pathname === "/home"
                                        ? "bg-blue-100 text-blue-700"
                                        : "text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                                }`}
                            >
                                <Home className="h-4 w-4 mr-1" />
                                Home
                            </Link>

                            <Link
                                to="/orders"
                                className={`flex items-center px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                    location.pathname === "/orders"
                                        ? "bg-blue-100 text-blue-700"
                                        : "text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                                }`}
                            >
                                <Package className="h-4 w-4 mr-1" />
                                Orders
                            </Link>

                            {/* Admin Dropdown */}
                            {authUser.isAdmin && (
                                <div className="relative">
                                    <button
                                        onClick={() => setIsAdminDropdownOpen(!isAdminDropdownOpen)}
                                        className={`flex items-center px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                            location.pathname.startsWith("/admin")
                                                ? "bg-purple-100 text-purple-700"
                                                : "text-purple-600 hover:text-purple-700 hover:bg-purple-50"
                                        }`}
                                    >
                                        <Shield className="h-4 w-4 mr-1" />
                                        Admin Panel
                                        <ChevronDown className={`h-4 w-4 ml-1 transition-transform ${isAdminDropdownOpen ? 'rotate-180' : ''}`} />
                                    </button>

                                    {/* Admin Dropdown Menu */}
                                    {isAdminDropdownOpen && (
                                        <div className="absolute left-0 mt-2 w-48 bg-white rounded-md shadow-lg border border-gray-200 z-50">
                                            <div className="py-1">
                                                <Link
                                                    to="/admin/articles"
                                                    onClick={() => setIsAdminDropdownOpen(false)}
                                                    className={`flex items-center px-4 py-2 text-sm transition-colors ${
                                                        location.pathname === "/admin/articles"
                                                            ? "bg-purple-50 text-purple-700"
                                                            : "text-gray-700 hover:bg-gray-50"
                                                    }`}
                                                >
                                                    <FileText className="h-4 w-4 mr-3" />
                                                    Manage Articles
                                                </Link>
                                                <Link
                                                    to="/admin/orders"
                                                    onClick={() => setIsAdminDropdownOpen(false)}
                                                    className={`flex items-center px-4 py-2 text-sm transition-colors ${
                                                        location.pathname === "/admin/orders"
                                                            ? "bg-purple-50 text-purple-700"
                                                            : "text-gray-700 hover:bg-gray-50"
                                                    }`}
                                                >
                                                    <Users className="h-4 w-4 mr-3" />
                                                    View Orders
                                                </Link>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            )}
                        </div>

                        {/* Cart & User Actions */}
                        <div className="flex items-center space-x-3 ml-6 pl-6 border-l border-gray-200">
                            {/* Cart Icon */}
                            <Link
                                to="/cart"
                                className="relative p-2 text-gray-500 hover:text-gray-700 transition-colors"
                            >
                                <ShoppingCart className="h-6 w-6" />
                                {totalItems > 0 && (
                                    <span className="absolute -top-1 -right-1 bg-blue-600 text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center">
                                        {totalItems > 99 ? "99+" : totalItems}
                                    </span>
                                )}
                            </Link>

                            {/* User Menu */}
                            <div className="flex items-center space-x-3">
                                <div className="flex items-center">
                                    <div className="bg-gray-200 p-2 rounded-full mr-2">
                                        <User className="h-4 w-4 text-gray-600" />
                                    </div>
                                    <div>
                                        <div className="text-sm font-medium text-gray-900">
                                            {authUser.name}
                                        </div>
                                        {authUser.isAdmin && (
                                            <div className="text-xs text-purple-600 font-medium">
                                                Admin
                                            </div>
                                        )}
                                    </div>
                                </div>

                                <button
                                    onClick={handleLogout}
                                    className="flex items-center px-3 py-2 text-sm font-medium text-gray-700 hover:text-red-700 hover:bg-red-50 rounded-md transition-colors"
                                >
                                    <LogOut className="h-4 w-4 mr-1" />
                                    Logout
                                </button>
                            </div>
                        </div>
                    </div>

                    {/* Mobile menu button */}
                    <div className="md:hidden flex items-center">
                        <button
                            onClick={() => setIsMenuOpen(!isMenuOpen)}
                            className="text-gray-600 hover:text-gray-900 hover:bg-gray-100 p-2 rounded-md"
                        >
                            {isMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
                        </button>
                    </div>
                </div>

                {/* Mobile Navigation */}
                {isMenuOpen && (
                    <div className="md:hidden">
                        <div className="px-2 pt-2 pb-3 space-y-1 border-t border-gray-200">
                            <Link
                                to="/home"
                                onClick={closeMenus}
                                className={`flex items-center px-3 py-2 rounded-md text-base font-medium ${
                                    location.pathname === "/home"
                                        ? "bg-blue-100 text-blue-700"
                                        : "text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                                }`}
                            >
                                <Home className="h-5 w-5 mr-3" />
                                Home
                            </Link>

                            <Link
                                to="/cart"
                                onClick={closeMenus}
                                className="flex items-center justify-between px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                            >
                                <div className="flex items-center">
                                    <ShoppingCart className="h-5 w-5 mr-3" />
                                    Cart
                                </div>
                                {totalItems > 0 && (
                                    <span className="bg-blue-600 text-white text-xs font-bold rounded-full h-6 w-6 flex items-center justify-center">
                                        {totalItems > 99 ? "99+" : totalItems}
                                    </span>
                                )}
                            </Link>

                            <Link
                                to="/orders"
                                onClick={closeMenus}
                                className={`flex items-center px-3 py-2 rounded-md text-base font-medium ${
                                    location.pathname === "/orders"
                                        ? "bg-blue-100 text-blue-700"
                                        : "text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                                }`}
                            >
                                <Package className="h-5 w-5 mr-3" />
                                Orders
                            </Link>

                            {/* Admin Menu - Mobile */}
                            {authUser.isAdmin && (
                                <>
                                    <div className="border-t border-gray-200 pt-3 mt-3">
                                        <div className="px-3 py-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">
                                            Admin Panel
                                        </div>
                                    </div>

                                    <Link
                                        to="/admin/articles"
                                        onClick={closeMenus}
                                        className={`flex items-center px-3 py-2 rounded-md text-base font-medium ${
                                            location.pathname === "/admin/articles"
                                                ? "bg-purple-100 text-purple-700"
                                                : "text-purple-600 hover:text-purple-700 hover:bg-purple-50"
                                        }`}
                                    >
                                        <FileText className="h-5 w-5 mr-3" />
                                        Manage Articles
                                    </Link>

                                    <Link
                                        to="/admin/orders"
                                        onClick={closeMenus}
                                        className={`flex items-center px-3 py-2 rounded-md text-base font-medium ${
                                            location.pathname === "/admin/orders"
                                                ? "bg-purple-100 text-purple-700"
                                                : "text-purple-600 hover:text-purple-700 hover:bg-purple-50"
                                        }`}
                                    >
                                        <Users className="h-5 w-5 mr-3" />
                                        View Orders
                                    </Link>
                                </>
                            )}

                            {/* User Info & Logout - Mobile */}
                            <div className="border-t border-gray-200 pt-3 mt-3">
                                <div className="flex items-center px-3 py-2">
                                    <div className="bg-gray-200 p-2 rounded-full mr-3">
                                        <User className="h-5 w-5 text-gray-600" />
                                    </div>
                                    <div>
                                        <div className="text-base font-medium text-gray-900">
                                            {authUser.name}
                                        </div>
                                        <div className="text-sm text-gray-500">
                                            {authUser.email}
                                        </div>
                                        {authUser.isAdmin && (
                                            <div className="text-sm text-purple-600 font-medium">
                                                Admin
                                            </div>
                                        )}
                                    </div>
                                </div>

                                <button
                                    onClick={handleLogout}
                                    className="flex items-center w-full px-3 py-2 text-base font-medium text-gray-700 hover:text-red-700 hover:bg-red-50 rounded-md transition-colors"
                                >
                                    <LogOut className="h-5 w-5 mr-3" />
                                    Logout
                                </button>
                            </div>
                        </div>
                    </div>
                )}
            </div>

            {/* Close dropdown when clicking outside */}
            {isAdminDropdownOpen && (
                <div
                    className="fixed inset-0 z-40"
                    onClick={() => setIsAdminDropdownOpen(false)}
                ></div>
            )}
        </nav>
    );
};

export default Navbar;